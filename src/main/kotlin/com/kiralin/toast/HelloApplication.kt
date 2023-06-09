package com.kiralin.toast

import javafx.animation.FadeTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File

enum class ImageStyle {
    CIRCLE, RECTANGLE
}

class Config {
    var alpha = 0.9
    var openTime = 30000.0
    var imageType = ImageStyle.RECTANGLE
    var title = "TITLE"
    var message = "MESSAGE"
    var appName = "APP NAME"
    var image = "https://product-image.juniqe-production.juniqe.com/media/catalog/product/seo-cache/x800/84/107/84-107-101X/Summer-Time-Cassia-Beck-Poster.jpg"
}

class Toast {
    private var config = Config()
    private val windows = Stage()
    private var root = BorderPane()
    private var box = HBox()


    class Builder {
        private var config = Config()

        fun setTitle(str: String): Builder {
            config.title = str
            return this
        }

        fun setMessage(str: String): Builder {
            config.message = str;
            return this
        }

        fun setAppName(str: String): Builder {
            config.appName = str
            return this
        }

        fun build(): Toast  {
            var toast = Toast()
            toast.config = config
            toast.build()

            return toast
        }
    }

    private fun build() {
        val width = 600.0
        val height = 300.0

        val vbox = VBox()

        val title = Label(config.title)
        val message = Label(config.message)
        val appName = Label(config.appName)

        title.isWrapText = true
        message.isWrapText = true
        appName.isWrapText = true

        var btnClose = Button("Закрыть")
        btnClose.style = "-fx-background-color: #5d7994"
        btnClose.onAction = EventHandler {
            windows.close()
        }
        root.children.add(btnClose)

        var btnUpperRight = Button("Правый верхний угол")
        btnUpperRight.style = "-fx-background-color: #cee1f2"
        btnUpperRight.onAction = EventHandler {
            windows.y = 0.0
            windows.x = Screen.getPrimary().bounds.maxX - width
        }
        root.children.add(btnUpperRight)

        var btnUpperLeft = Button("Левый верхний угол")
        btnUpperLeft.style = "-fx-background-color: #cee1f2"
        btnUpperLeft.onAction = EventHandler {
            windows.y = 0.0
            windows.x = 0.0
        }
        root.children.add(btnUpperLeft)

        var btnLowerLeft = Button("Левый нижний угол")
        btnLowerLeft.style = "-fx-background-color: #cee1f2"
        btnLowerLeft.onAction = EventHandler {
            windows.y = Screen.getPrimary().bounds.maxY - height - 40.0
            windows.x = 0.0
        }
        root.children.add(btnLowerLeft)

        var btnLowerRight = Button("Нижний правый угол")
        btnLowerRight.style = "-fx-background-color: #cee1f2"
        btnLowerRight.onAction = EventHandler {
            windows.x = Screen.getPrimary().bounds.maxX - width
            windows.y = Screen.getPrimary().bounds.maxY - height - 40.0
        }
        root.children.add(btnLowerRight)

        var btnChangeImageType = Button("Изменить форму изображения")
        btnChangeImageType.style = "-fx-background-color: #cee1f2; -fx-font-weight:bold"
        btnChangeImageType.onAction = EventHandler {
            if (config.imageType == ImageStyle.RECTANGLE) {
                config.imageType = ImageStyle.CIRCLE
            } else {
                config.imageType = ImageStyle.RECTANGLE
            }
            box.children.removeAt(0)
            setImage()
        }
        root.children.add(btnChangeImageType)

        windows.initStyle(StageStyle.TRANSPARENT)

        windows.x = Screen.getPrimary().bounds.maxX - width //дефолтные значения
        windows.y = Screen.getPrimary().bounds.maxY - height - 40.0

        windows.scene = Scene(root, width, height)
        windows.scene.fill = Color.TRANSPARENT

        root.style = "-fx-background-color: #77a7d4"
        root.setPrefSize(width, height)

        title.style = "-fx-text-fill: white"
        message.style = "-fx-text-fill: white"
        appName.style = "-fx-text-fill: white"

        setImage()

        vbox.children.addAll(title, message, appName, btnClose, btnUpperLeft, btnUpperRight, btnLowerLeft, btnLowerRight, btnChangeImageType)
        box.children.add(vbox)
        root.center = box
    }

    private fun setImage() {
        if (config.image.isEmpty()) {
            return
        }
        val iconBorder = if (config.imageType == ImageStyle.RECTANGLE) {
            Rectangle(300.0, 300.0)
        }
        else {
            Circle(150.0, 150.0, 150.0)
        }
        iconBorder.setFill(ImagePattern(Image(config.image)))
        box.children.add(0, iconBorder)
    }

    private fun openAnimation() {
        val anim = FadeTransition(Duration.millis(1500.0), root)
        anim.fromValue = 0.0
        anim.toValue = config.alpha
        anim.cycleCount = 1
        anim.play()
    }
    private fun closeAnimation() {
        val anim = FadeTransition(Duration.millis(1500.0), root)
        anim.fromValue = config.alpha
        anim.toValue = 0.0
        anim.cycleCount = 1
        anim.onFinished = EventHandler {
            Platform.exit()
            System.exit(0)
        }
        anim.play()
    }

    fun start() {
        windows.show()
        openAnimation();
        val thread = Thread {
            try {
                Thread.sleep(config.openTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            closeAnimation()
        }
        Thread(thread).start()
    }
}

private fun music(){
    val musicFile = "C:\\zvuk-soobsheniya-vk-1.mp3"
    val sound = Media(File(musicFile).toURI().toString())
    val mediaPlayer = MediaPlayer(sound)
    mediaPlayer.play()
}

class SomeClass: Application() {
    override fun start(p0: Stage?) {
        music()
        var toast = Toast.Builder()
            .setTitle("Задание выполнено:")
            .setMessage("Первая Лаба")
            .setAppName("Чоопо")
            .build()
        toast.start()
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SomeClass::class.java)
        }
    }
}