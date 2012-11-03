lockout
=======

lockout is a simple RSS reader app for Android.

This is just a toy project to explore the basics of the Android SDK.
Feel free to incorporate this code into your own real apps.

Upon startup, the main activity instantiates an RSSHandler which
requests the feed by its URL and parses it. The results are gathered
synchronously and displayed to the user as a list view. Each article
has a URL to its full version so touching a title will bring up a
browser with the full article.
