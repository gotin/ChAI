package hello

/* import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane

object HelloSBT extends JFXApp {
  stage = new PrimaryStage {
    scene = new Scene {
      root = new BorderPane {
        padding = Insets(25)
        center = new Label("Hello SBT")
      }
    }
  }
}

*/

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

object HelloStageDemo extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Stage"
    width = 600
    height = 450
    scene = new Scene {
      fill = Color.LightGreen
      content = new Rectangle {
        x = 25
        y = 40
        width = 100
        height = 100
        fill <== when (hover) choose Color.Green otherwise Color.Red
      }
    }
  }
}
