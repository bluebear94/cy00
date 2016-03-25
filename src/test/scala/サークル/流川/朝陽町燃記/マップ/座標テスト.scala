package サークル.流川.朝陽町燃記.マップ

import org.scalatest._

/**
 * @author ウルヰ
 */
class 座標テスト extends FlatSpec with ShouldMatchers {
  val ここ = 座標(34, 323)
  val そこ = 座標(37, 317)
  val あそこ = 座標(-98, 274)
  "列" should "get X coord" in {
    ここ.列 should equal(34)
    そこ.列 should equal(37)
    あそこ.列 should equal(-98)
  }
  "行" should "get Y coord" in {
    ここ.行 should equal(323)
    そこ.行 should equal(317)
    あそこ.行 should equal(274)
  }
  "列化" should "change X coord" in {
    ここ.列化(-1) should equal (座標(-1, 323))
    そこ.列化(-892) should equal (座標(-892, 317))
    あそこ.列化(0) should equal (座標(0, 274))
  }
  "行化" should "change Y coord" in {
    ここ.行化(89) should equal (座標(34, 89))
    そこ.行化(98) should equal (座標(37, 98))
    あそこ.行化(100) should equal (座標(-98, 100))
  }
  "左" should "subtract from X coord" in {
    ここ.左 should equal (座標(33, 323))
    そこ.左(3) should equal (座標(34, 317))
  }
  "右" should "add to X coord" in {
    ここ.右 should equal (座標(35, 323))
    そこ.右(3) should equal (座標(40, 317))
  }
  "上" should "subtract from Y coord" in {
    ここ.上 should equal (座標(34, 322))
    そこ.上(3) should equal (座標(37, 314))
  }
  "下" should "add to Y coord" in {
    ここ.下 should equal (座標(34, 324))
    そこ.下(3) should equal (座標(37, 320))
  }
}