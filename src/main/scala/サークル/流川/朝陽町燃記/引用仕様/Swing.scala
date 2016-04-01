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
  override def main(引数: Array[String]) =
    super[いんようしよう].main(引数)
  def top = new MainFrame {
    title = 現代言語.タイトル
    contents = new BoxPanel(Orientation.Horizontal) {
      contents += 画面
      contents += メッセージ画面
      focusable = true
      requestFocus()
      listenTo(keys, 画面.keys, メッセージ画面.keys)
    }
  }
  def スタート(せ: 設定) {
    println("妖夢です")
    val げ = new ゲーム("test", this)
    げ.初期化()
    super[SimpleSwingApplication].main(Array[String]())
  }
}

class キャンバス extends Panel {
  preferredSize = new Dimension(640, 480)
  override def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
  }
}