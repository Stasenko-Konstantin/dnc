import java.awt.{Font, GridLayout}
import java.time.Year
import java.time.format.DateTimeFormatter
import javax.swing.{JButton, JFrame, JLabel, JMenu, JMenuBar, JMenuItem, JPanel, JPopupMenu, JTextField, SwingConstants}
import scala.sys.exit
import scala.util.Try
import scala.util.control.Breaks.{break, breakable}

@main
def main(): Unit =
  val frame = JFrame("dnc")
  frame setSize (400, 170)
  frame setResizable false

  val menuBar = JMenuBar()
  menuBar add {
    val menu = JMenu("Меню")
    def createFrame(name: String, text: String, fontSize: Int): JFrame =
      val f = JFrame(name)
      f setSize (300, 200)
      f setResizable false
      f add {
        val label = JLabel(text, SwingConstants.CENTER)
        val font = label.getFont
        label.setFont(Font(font.getFontName(), Font.PLAIN, fontSize))
        label
      }
      f
    val aboutProgram = "О программе"
    val aboutAuthor = "Об авторе"
    val aboutProgramText = "<html>Учебная программа для расчета<br>ближайшего счастливого года"
    val aboutAuthorText = "<html>Стасенко К.Ю.<br>github.com/Stasenko-Konstantin<br>stasenko.kost@yandex.ru"
    val frames = Map[String, JFrame](
      aboutProgram -> createFrame(aboutProgram, aboutProgramText, 16),
      aboutAuthor -> createFrame(aboutAuthor, aboutAuthorText, 15)
    )
    def checkFrameVisibility(name: String): Unit =
      val frame = frames(name)
      frame setVisible !frame.isVisible
    menu adds (
      { val p = JMenuItem(aboutProgram); p.addActionListener(_ => checkFrameVisibility(aboutProgram)); p },
      { val a = JMenuItem(aboutAuthor); a.addActionListener(_ => checkFrameVisibility(aboutAuthor)); a },
      JPopupMenu.Separator(),
      { val e = JMenuItem("Выход"); e.addActionListener(_ => exit(0)); e }
    )
    menu
  }
  frame setJMenuBar menuBar

  val panel = JPanel()
  val taskText = "<html>Введите день и месяц своего рождения<br>" +
    "После нажатия кнопки \"Ок\" выведется ваш<br>" +
    "ближайший счастливый год"
  val answerLabel = JLabel()
  val dayField = JTextField(2)
  val monthField = JTextField(2)
  panel adds (
    JLabel(taskText),
    { val p = JPanel(); p.setLayout(GridLayout()); p adds
        (
          JLabel("День: "),
          dayField,
          JLabel("Месяц: "),
          monthField,
          { val b = JButton("Ok");
            b.addActionListener(_ => checkHappyYear(dayField.getText(), monthField.getText(), answerLabel))
            b
          }
        )
      p
    },
    { val p = JPanel(); p.setLayout(GridLayout()); p adds (JLabel("Ваш ближайший год: "), answerLabel); p }
  )
  frame add panel

  frame setVisible true
end main

def checkHappyYear(day: String, month: String, answerL: JLabel): Unit =
  if day.toIntOption.isDefined && month.toIntOption.isDefined then
    if !isCorrectDate(day, month) then return
    val dayRem = day.foldRight(0)((c, sum) => sum + c.toString.toInt) % 10
    val monthRem = month.foldRight(0)((c, sum) => sum + c.toString.toInt) % 10
    breakable:
      var i = Year.now.getValue
      for year <- LazyList.continually(i) do
        i += 1
        val yearSum = year.toString.foldRight(0)((c, sum) => sum + c.toString.toInt)
        val yearRem = yearSum % 10
        if yearRem == dayRem || yearRem == monthRem then
          answerL.setText(year.toString)
          break
end checkHappyYear

def isCorrectDate(day: String, month: String): Boolean =
  Try(DateTimeFormatter.ofPattern("dd/MM/yyyy").parse(day+"/"+month+"/"+"2000")).isSuccess
end isCorrectDate

extension (r: JMenu|JPanel)
  def adds(cs: Any*): Unit = for c <- cs do c match
    case i: JMenuItem => r add i
    case s: JPopupMenu.Separator => r add s
    case l: JLabel => r add l
    case p: JPanel => r add p
    case f: JTextField => r add f
    case b: JButton => r add b
  end adds
end extension
