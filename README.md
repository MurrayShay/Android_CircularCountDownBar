# CircularCountDownBar
![github](https://github.com/MurrayShay/Android_CircularCountDownBar/blob/master/CircularCountDownBar.gif "github")

Simple single android view component that can be used to showing a round progress bar. It can be customized with
size, stroke size, colors and text etc. See image for some example. Progress change will be animated.

How to create circular count down progress bar programmatically in android.  The smallest size specified
for width and height will be used as diameter of the circle.

``` JAVA
       CircularCountDownBar countDownBar = new CircularCountDownBar.Builder(this)
                       .setMaxProgress(20)  // time seconds count down setting
                       .setProgressColor(Color.BLUE)
                       .setStrokeWidth(30)
                       .setTextColor(Color.RED)
                       .setPadding(15,15,15,15)
                       .setRoundedCorners(false)
                       .setDrawText(true)
                       .build();

       LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(350,350);
       params1.gravity = Gravity.CENTER;
       params1.topMargin = 30;

       linearLayout.addView(countDownBar, params);
```

Schedule a countdown until a time in the future, with regular notifications on intervals along the way.
Example of showing a 30 second countdown in a text field:The calls to onTick(long) are synchronized to
this object so that one call to onTick(long) won't ever occur before the previous callback is complete.
This is only relevant when the implementation of onTick(long) takes an amount of time to execute that is
significant compared to the countdown interval.

``` JAVA
        new CountDownTimer((cd *= 1000) + timeOffset, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    countDownBar.setProgress((int)(millisUntilFinished/1000));
                }

                @Override
                public void onFinish() {

                }

        }.start();
```

You can change the progress calling (with a value between 0 and MaxProgress):

``` JAVA
       public void setProgress(int progress)
```

The progress bar is customizable by changing any of the available settings
using innerBuilder design pattern like alertDialog in android.git

``` JAVA
        public Builder setMaxProgress(int maxProgress)
        public Builder setProgressColor(int color)
        public Builder setProgressWidth(int width)
        public Builder setTextColor(int color)
        public Builder showProgressText(boolean show)
        public Builder useRoundedCorners(boolean roundedCorners)
        public Builder setPadding(int top, int bottom, int left, int right)
```