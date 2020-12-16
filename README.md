# FX Demo #

A demo leginkabb azert keszult, hogy szemugyre lehessen venni ket dolgot

- hogyan szokott kinezni egy GUI alkalmazas
- hogyan lehet elkerulni egy felhasznaloi felulet programozott felepiteset

A forditashoz (szerintem) legalabb Java 11-es JDK kell.

## GUI mukodese ##

A GUI-k altalanos mukodesi elve a callback-ek, valamint az MVC architektura szeleskoru alkalmazasa. Az MVC (
Model-Viewer-Controller) haromsag, harom kulonbozo programreszre biz harom kulonbozo feladatot. A _model_ ( a kodban
a `***Model` osztalyok) felelosek az adatok tarolasaert. Minden, mi megjelenitendo adat, vagy kapcsolat, ide kerul. A
_viewer_ maga a felhasznaloi felulet, a grafikai elemek, gombok, szovegmezok osszessege (ezt a Java FX keretrendszer
kepviseli). A _controller_ ( a kodban a `***Controller` osztalyok) pedig gondoskodik a progam logikajarol es a
felhasznalo akcioinak lekezeleserol.

Ha a felhasznalo csinal valamit, akkor egy _controller_ osztaly egyik metodusa hivodik meg (a kodban ezek `@FXML`
annatacioval vannak megjelolve), amely tipikusan megvaltoztatja a _model_t. A _viewer_ eszleli a valtoztatast, es
frissiti felhasznaloi feluletet.

## GUI felepitese ##

A kodban ott tortenik a GUI felepitese ahol az `FXMLLoaader.load()` van meghivva. Ez egy (F)XML file-t tolt be, amely
tartalmazza a program egy grafikai reszletnek leirasat (egy ablak, vagy egy dialogusdoboz)

## Mit csinal a program ##

A menubol a `Connect...` menut valasztva egy Kafka szerverhez torteno csatlakozashoz szukseges parametereket bekero
dialogusablakhoz jutunk. Ez utan a `Open` menu lekeri a topicok listajat, amelyek egy lista nezetben lathatoak. Egy
topicot kivalasztva, par reszlet (particiok, isr, stb) jelenik meg rola egy tablazatban.