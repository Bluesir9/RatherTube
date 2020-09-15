package extensions

import com.soywiz.klock.TimeSpan
import org.w3c.dom.Audio

val Audio.playedLength get() = TimeSpan(this.currentTime * 1000)
