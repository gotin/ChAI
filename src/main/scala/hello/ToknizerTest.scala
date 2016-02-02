/**
  * Created by shinya on 2016/01/17.
  */

import org.atilika.kuromoji.{Token, Tokenizer}

sealed trait SFCompare
case class SFPOFPair(sf:String,pofs:List[String]) extends SFCompare

object MMain extends App {

  // type SFPOFPair = SFPOFPair

  // val tokenizer = Tokenizer.builder.mode(Tokenizer.Mode.NORMAL).build
  // val tokens = tokenizer.tokenize("僕の夢は、不労収入を得て一生遊んで暮らすことです。").toArray
  val str:String ="僕の夢は、不労収入を得て一生遊んで暮らすことです。"
  val t = generateTokenList(str)
      t.foreach(t => println(t.sf +t.pofs.foreach( f => print(f))))

  /*  tokens.foreach { t =>
      val token = t.asInstanceOf[Token]
      println(s"${token.getSurfaceForm} - ${token.getAllFeatures}")
      // println(s"${token.getSurfaceForm} - ${token.getAllFeatures}")
      System.out.println("allFeatures : " + token.getAllFeatures());
      System.out.println("partOfSpeech : " + token.getPartOfSpeech());
      System.out.println("position : " + token.getPosition());
      System.out.println("reading : " + token.getReading());
      System.out.println("surfaceFrom : " + token.getSurfaceForm());
      // System.out.println("allFeaturesArray : " + Arrays.asList(token.getAllFeaturesArray()));
      System.out.println("辞書にある言葉? : " + token.isKnown());
      System.out.println("未知語? : " + token.isUnknown());
      System.out.println("ユーザ定義? : " + token.isUser());
  } */

  def generateTokenList(s:String):List[SFPOFPair] = {
    val tokenizer = Tokenizer.builder.mode(Tokenizer.Mode.NORMAL).build
    val tokenList = tokenizer.tokenize(s).toArray.toList.reverse
    val structList: List[SFPOFPair] = List()
    recuAnalyzeToken(tokenList, structList)
  }

  def recuAnalyzeToken(tokenList:List[AnyRef],structList:List[SFPOFPair]):List[SFPOFPair] = {
    if (tokenList.length == 0) return structList
    val token = tokenList.head.asInstanceOf[Token]
    val sfpofs:SFPOFPair = SFPOFPair(token.getSurfaceForm(),token.getPartOfSpeech().split(",").toList.filter(_ != "*"))
    println("bbb "+sfpofs.sf+" ")
    recuAnalyzeToken(tokenList.tail,sfpofs::structList)
  }
}
