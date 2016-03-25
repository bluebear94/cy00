package サークル.流川.朝陽町燃記.マップ

case class ノード(
    val どこ: 座標, // 8
    val 温度: Short, // 10
    val 湿度: Short, // 12
    val 町偏り列: Byte,
    val 町偏り行: Byte,
    val 町大きさ: Byte // 15バイト
) {
  
}