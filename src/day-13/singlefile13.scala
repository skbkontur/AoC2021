//working version here https://github.com/FunFunFine/cli/blob/master/src/main/scala/private/DayThirteen.scala

import zio.*
import zio.Console.*
import language.experimental.fewerBraces
import zio.*
import zio.Console.*
import zio.stream.*
import java.nio.file.*

//build.sbt
/*
val zioVersion = "2.0.0-M6-2"

scalaVersion := "3.1.2-RC1-bin-20211213-8e1054e-NIGHTLY"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio"          % zioVersion,
  "dev.zio" %% "zio-streams"          % zioVersion,
)
*/
object DayThirteen extends ZIOAppDefault:
  def run =
    for
      raw      <- readInput("paper.txt")
      taskInput = Input.fromRaw(raw)
      paper     = taskInput.paper
      _        <- printLine(paper.show)
      _        <- printLine(taskInput.folds.mkString("\n"))
      answer    = taskInput.folds.foldLeft(paper)(_.fold(_))
      _        <- printLine(answer.show)
      _        <- writeOutput("code.txt")(answer.show)
    yield ()



case class Point(x: Int, y: Int)


enum Axis:
  case X
  case Y

object Axis:
  def unapply(s: String): Option[Axis] =
    s match
      case "x" => Some(Axis.X)
      case "y" => Some(Axis.Y)
      case _   => None


case class Fold(along: Axis, at: Int)

object Fold:
  def parse(raw: String): Option[Fold] =
    raw match
      case s"fold along ${Axis(axis)}=${value}" =>
        value.toIntOption.map(Fold(axis, _))
      case _ => None


case class Input(dots: List[Point], folds: List[Fold]):
  val paper: Paper =
    Paper(dots.toSet)

object Input:
  def fromRaw(data: List[String]): Input =
    val paperDataRaw = 
      data
      .takeWhile(_ != "")
      .map(_.split(",")
            .map(_.toIntOption))

    val paperData = paperDataRaw
      .collect:
        case Array(Some(x), Some(y)) => Point(x, y)
      
    val foldsData = 
      data.drop(paperData.size + 1)
          .map(Fold.parse)
          .collect:
            case Some(fold) => fold
    Input(paperData, foldsData)


case class Paper(dots: Set[Point]):
  val width  = dots.maxBy(_.x).x
  val height = dots.maxBy(_.y).y

  def fold(fold: Fold): Paper =
    fold.along match
      case Axis.X =>
        val (rightPoints, leftPoints) = dots.partition:
          case Point(x, y) => x >= fold.at
      
        val invertedRightPoints       = rightPoints.map { case Point(x, y) =>
          Point(fold.at - Math.abs(fold.at - x), y)
        }
        Paper(invertedRightPoints) <+> Paper(leftPoints)

      case Axis.Y =>
        val (pointsBelow, pointsAbove) = dots.partition:
           case Point(x, y) => y >= fold.at
        
        val invertedPointsBelow        = pointsBelow.map:
         case Point(x, y) =>
          Point(x, fold.at - Math.abs(fold.at - y))
        
        Paper(invertedPointsBelow) <+> Paper(pointsAbove)

object Paper:
  given Show[Paper] =
    paper => 
      val xs = 0 to paper.width
      val ys = 0 to paper.height
      val sb = StringBuilder()
      ys.foreach:
          y => 
              xs.foreach:
                  x =>
                      sb.append(if paper.dots.contains(Point(x, y)) then "⬛" else "⬜")
              sb.append("\n")
      sb.toString

  given Semigroup[Paper] with
    def combine(l: Paper, r: Paper): Paper =
      Paper(l.dots.union(r.dots))





trait Semigroup[A]:
  def combine(l: A, r: A): A
  extension (l: A) def <+>(r: A): A = combine(l, r)

object Semigroup:
  given Semigroup[Unit] = (_, _) => ()
  given Semigroup[Int]  = _ + _



trait Show[A]:
  def shown(a: A): String
  extension (a: A) def show: String = shown(a)

object Show:
  given Show[String] = identity(_)
  given Show[Unit]   = _.toString



/** Writes to repository directory */
def writeOutput(filename: String)(data: String): ZIO[Any, Throwable, Unit] = 
    def fileSink(path: Path) =
        ZSink
            .fromFile(path)
            .contramapChunks[String](_.flatMap(_.getBytes))

    ZStream(data).run(fileSink(Paths.get("code.txt"))).ignore

/** Reads from repository directory */
def readInput(filename: String): ZIO[Any, Throwable, List[String]] =
  ZStream
    .fromFile(Paths.get(filename))
    .via(ZPipeline.utf8Decode >>> ZPipeline.splitLines)
    .runCollect
    .map(_.toList)
     

//@scala_ru



