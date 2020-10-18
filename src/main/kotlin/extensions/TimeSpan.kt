package extensions

import com.soywiz.klock.TimeSpan

/*
If TimeSpan in question is say 175 seconds,
this will return -> 02:55

If TimeSpan in question is say 11 minutes
and 33 seconds, this will return -> 11:33
*/
fun TimeSpan.getAsPlaybackTimestamp(): String {
  val calculatedMinutes = (this.seconds / 60).toInt()
  val leftoverSeconds = (this.seconds % 60).toInt()
  val minutesString =
    if(calculatedMinutes >= 10) "$calculatedMinutes" else "0$calculatedMinutes"
  val secondsString =
    if(leftoverSeconds >= 10) "$leftoverSeconds" else "0$leftoverSeconds"
  return "$minutesString:$secondsString"
}