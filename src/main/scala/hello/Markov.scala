/**
  * Created by shinya on 2016/01/23.
  * https://github.com/yuroyoro/scala-hackathon/blob/master/doc/source/sample/markov.rst
  */

import java.net.URLEncoder

import scala.collection.mutable.ListBuffer
import scala.collection.immutable.Map
import scala.io.Source
import scala.util.Random
import scala.xml._

case class Morpheme( word:String, pos:String, startFlag:Boolean, endFlag:Boolean)

/**
  * Yahoo形態素解析APIを利用して形態素解析を行うObject
  */
object MorphologicalAnalyser {

  val appIDYahoo="dj0zaiZpPTlDZDJIaXdpTm51MiZzPWNvbnN1bWVyc2VjcmV0Jng9NTA-"
  val secretYahoo = "acdf2937cf1f3bbd08e237a7e0e94202f8350071"
  val url = "http://jlp.yahooapis.jp/MAService/V1/parse?appid=%s&sentence=%s"

  /**
    * 形態素解析を行い、品詞分割されたListを返します。
    */
  def apply( s:String ) = {
    XML.loadString(
      Source.fromURL(
        url.format( appIDYahoo, URLEncoder.encode(s , "utf-8")),"utf-8").getLines.mkString
    ) \\ "word" map{ word => Morpheme(word \\ "surface" text, word \\ "pos" text,false,false) }
    // println(xml.toString())
    // println(wordList.toString())
  }
}

/**
  * 与えられた文章からマルコフ連鎖します。
  */

class Markov( sentence:Seq[String] ) {

  val rnd = new Random
  val maxKeyCnt = 3
  var dict:Map[(Morpheme, Morpheme), List[Morpheme]] = Map.empty
  var terminater:ListBuffer[(Morpheme, Morpheme)] = new ListBuffer

  print("解析中")
  sentence.foreach{ s =>
    print(".")
    MorphologicalAnalyser( s ) match {
      case ms if ms.length < 2 =>
      case ms =>
        // println(ms)
        // println(ms(0)+" "+ms(1))
        // println(ms.drop(2))
        terminater += ((ms(0),ms(1)) /: ms.drop(2)) {
          case (key, m) => dict.get(key) match {
            case None => dict += key -> (m :: Nil)
            case Some(xs) => dict += key -> (m :: xs)
          }
           // if( m.pos == "特殊" ) terminater += (key._2, m)
          (key._2,m)
        }
    }
  }
  println("dict")
  dict.foreach(x => {
      print(x._1._1.word+"-"+x._1._2.word+":")
      println(x._2.foreach(y => print(y.word+",")))
    })
  println("terminator"+terminater)
  println("done.")

  def generate( n:Int ):String = {
    if (n == 0) ""
    else {
      val (m1, m2) = terminater(rnd.nextInt(terminater.length))
      var selected = Map[(Morpheme, Morpheme), Int]((m1, m2) -> 1)

      def gen(key: (Morpheme, Morpheme)): String = {
        val selectedCnt = selected.get(key).getOrElse(0) + 1
        if (selectedCnt >= maxKeyCnt) ""
        else {
          selected += key -> selectedCnt
          dict.get(key) match {
            case None => ""
            case Some(Nil) => ""
            case Some(xs) => {
              val m = xs(rnd.nextInt(xs.length))
              m.word + gen((key._2, m))
            }
          }
        }
      }
      m1.word + m2.word + gen((m1, m2)) + generate(n - 1)
    }
  }
}


object Main{
  def main( args:Array[String] ) = {
    println("Feed(RSS1.0/RSS2.0)のURLを入力してください。")
    print(" > ")
    val url = "http://gyao.yahoo.co.jp/rss/newlypg/all/"
    val source = Source.fromURL(url,"utf-8")
    val xml = XML.loadString(source.getLines.mkString)
    val nodeList = (xml \\ "item" \\ "description").toList
    var textList:List[String] = List()
    nodeList.foreach(node => textList = node.text::textList)
    println(textList)
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