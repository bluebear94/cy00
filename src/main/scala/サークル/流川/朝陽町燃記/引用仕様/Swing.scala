package サークル.流川.朝陽町燃記.引用仕様

import scala.swing._
import scala.swing.event._
import scala.swing.BorderPanel.Position._
import java.awt.{ Graphics2D, Color, Dimension, Font }
import java.awt.image.BufferedImage
import サークル.流川.朝陽町燃記._

/**
 * @author ウルヰ
 */
case object Swing extends SimpleSwingApplication
    with いんようしよう {
  val モノスペース = new Font("VL Gothic", 0, 16)
  val 画面 = new キャンバス()
  val メッセージ画面 = new TextArea(20, 50) {
    font = モノスペース
    editable = false
    charWrap = true
    text = "朝陽町燃記にようこそ"
    lineWrap = true
    background = new Color(0, 60, 0)
    foreground = Color.WHITE
  }
  val バー = new ProgressBar() {
    background = Color.BLACK
    foreground = Color.GREEN
    preferredSize = new Dimension(640, 20)
    min = 0
    max = 640
    visible = false
  }
  override def main(引数: Array[String]) =
    super[いんようしよう].main(引数)
  def top = new MainFrame {
    title = 現代言語.タイトル
    contents = new BoxPanel(Orientation.Vertical) {
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += 画面
        contents += メッセージ画面
      }
      contents += バー
      focusable = true
      requestFocus()
      listenTo(keys, 画面.keys, メッセージ画面.keys)
    }
  }
  def スタート(せ: 設定) {
    println("妖夢です")
    super[SimpleSwingApplication].main(Array[String]())
    げ.初期化()
  }
  def ロードバー表示(): Unit = {
    バー.visible = true
  }
  def ロードバー非表示(): Unit = {
    バー.visible = false
  }
  def ロードバー進捗(分数: Double): Unit = {
    バー.value = (分数 * 640).toInt
  }
  def プリント(メッセージ: String): Unit = {
    メッセージ画面.text += "\n" + メッセージ
  }
}

class キャンバス extends Panel {
  preferredSize = new Dimension(640, 480)
  override def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
  }
}