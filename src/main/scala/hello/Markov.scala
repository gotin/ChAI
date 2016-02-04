/**
  * Created by shinya on 2016/01/23.
  * https://github.com/yuroyoro/scala-hackathon/blob/master/doc/source/sample/markov.rst
  */

import java.net.URLEncoder

import scala.collection.mutable.ListBuffer
import scala.collection.immutable.Map
import scala.io.Source
import scala.util.Random
import scala.xml.XML

case class Morpheme( word:String, pos:String, startFlag:Boolean, endFlag:Boolean)
type MorphemePair = (Morpheme,Morpheme)
type MorphemeList = List[Morpheme]
type Dictionary = Map[MorphemePair, MorphemeList]
/**
  * Yahoo形態素解析APIを利用して形態素解析を行うObject
  */
object MorphologicalAnalyser {

  val APP_ID_YAHOO="dj0zaiZpPTlDZDJIaXdpTm51MiZzPWNvbnN1bWVyc2VjcmV0Jng9NTA-"
  val urlTemplate = "http://jlp.yahooapis.jp/MAService/V1/parse?appid=%s&sentence=%s"

  /**
    * 形態素解析を行い、品詞分割されたListを返します。
    */
  def apply( s:String ) = {
    val words = XML.loadString(
      Source.fromURL(
        urlTemplate.format(APP_ID_YAHOO, URLEncoder.encode(s, "utf-8")), "utf-8").getLines.mkString
    ) \\ "word"
    words.zipWithIndex.map( word_i => {
      val (word,i) = word_i
      Morpheme(word \\ "surface" text, word \\ "pos" text, i==0, i==words.length)
    })
  }
}

/**
  * 与えられた文章からマルコフ連鎖します。
  */

class Markov( sentence:Seq[String] ) {
  print("解析中")
  val (dict, terminater) = analyze(sentence)
  val starters = terminater.filter(_._1.startFlag)
  def analyze(s:Seq[String]): (Dictionary, List[MorphemePair]) = {
    val dict = s.map{ s =>
      print(".")
      MorphologicalAnalyser(s) match {
        case ms if ms.length < 2 => Map[MorphemePair,MorphemeList]()
        case ms =>
          ( (Map((ms(0),ms(1))->(ms(2)::Nil)),ms(1),ms(2)) /: ms.drop(2)) (
            (dict_1_2, xn) => {
              val (dict,x1,x2)=dict_1_2
              (dict + ((x1, x2)->(xn +: dict.getOrElse((x1,x2),List[Morpheme]()))), x2, xn)
            } )._1
      }
    }.reduce((a,b) => a ++ b)

    // printDict(dict)

    val terminater = dict.keys.toList
    // println(s"terminator: ${terminater}")
    // println("done.")
    (dict, terminater)
  }

  def printDict(dict:Dictionary):Unit = {
    println("dict")
    dict.foreach(kv => {
      val (mPair, mList) = kv
      val (m1, m2) = mPair
      print(s"${m1.word}-${m2.word}:")
      println(mList.map(_.word).mkString(","))
    })
  }

  val rnd = new Random
  val maxKeyCnt = 3

  def generate( n:Int ):String = {
    if (n == 0) return ""
    val (m1, m2) = starters(rnd.nextInt(starters.length))
    val selected = Map[MorphemePair, Int]((m1, m2) -> 1)
    val (phrase, _) = gen((m1,m2), selected)
    // selected = newSelected
    m1.word + m2.word + phrase +"\n" + generate(n - 1)
  }

  def gen(key: MorphemePair, selected:Map[MorphemePair, Int]): (String, Map[MorphemePair, Int]) = {
    val selectedCnt = selected.get(key).getOrElse(0) + 1
    if (selectedCnt >= maxKeyCnt) return ("", selected)
    val newSelected = selected + (key -> selectedCnt)
    dict.get(key) match {
      case Some(xs) => {
        val m = xs(rnd.nextInt(xs.length))
        val (phrase, newSelected2) = gen((key._2, m), newSelected)
        (m.word + phrase, newSelected2)
      }
      case _=> ("", newSelected)
    }
  }

}


object Main{
  def main( args:Array[String] ) = {
    // println("Feed(RSS1.0/RSS2.0)のURLを入力してください。")
    // print(" > ")
    val url = "http://gyao.yahoo.co.jp/rss/newlypg/all/"
    val source = Source.fromURL(url,"utf-8")
    val xml = XML.loadString(source.getLines.mkString)
    val textList = (xml \\ "item" \\ "description").map(n => n.text)
    textList.foreach(println)
    val markov = new Markov(textList)
    println ("生成文書:"+markov.generate(10))
    // val s = MorphologicalAnalyser(textList.head)
    // println(s)

    /*   val sentence = FeedCrawler.getSentence( url )
    val markov = new Markov( sentence )

    val num = """(\d+)""".r
    def cmd:Unit = {
      println("生成する文章の長さを入力してください。")
      print(" > ")
      Console.readLine match {
        case "quit" => System.exit(0)
        case num(cnt) => {
          println( markov.generate( cnt.toInt ) );
          cmd
        }
        case _ => cmd
      }
    } */
  }
}
