package extensions

import com.soywiz.klock.TimeSpan
import org.w3c.dom.Audio

val Audio.playedLength get() = TimeSpan(this.currentTime * 1000)

val Audio.totalLength get() = TimeSpan(this.duration * 1000)

fun Audio.seekTo(position: Double) {
  this.currentTime = position
}
