package webcrawler

import java.io.IOException
import java.net.SocketTimeoutException
import org.jsoup._

/**
  * Created by shinya on 2016/01/11.
  */
object WebCrawler {
  def googlePlayIconUrl(appIdentifier: String): Option[String] = {
    val googleImgTag = "div.cover-container img.cover-image"
    def googlePlayUrl(appIdentifier: String): String = s"https://play.google.com/store/apps/details?id=$appIdentifier"
    try {
      val doc = Jsoup.connect(googlePlayUrl(appIdentifier)).get
      Option(doc.select(googleImgTag).first.attr("src"))
    } catch {
      case e: HttpStatusException => {
        // loggerに関しては、適当なものを定義してください。
        System.err.println(s"GooglePlayにアクセスできません。$appIdentifier")
        None
      }
      case e: SocketTimeoutException => {
        System.err.println(s"GooglePlayにタイムアウトでアクセスできません。$appIdentifier")
        None
      }
      case e: IOException => None
    }
  }
}
