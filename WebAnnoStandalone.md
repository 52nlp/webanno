## Quick start ##

All-in-one version which does not require a database server or tomcat to be set up.

Get the standalone jar from the [downloads](WebAnnoInstFile.md) and start it simply with a **double-click** in your file manager. WebAnno stores its data in a folder called `.webanno` (_dot webanno_) within your home folder,

You can start with the [example projects](SampleProjects.md) to explore some of the functionalities.

## Important notice ##

**Note:** If you have previously used an older version of the WebAnno standalone, make sure to clean up the temporary installation before running the new version:

  * On OS X: `rm -R "$TMPDIR/winstoneEmbeddedWAR"`
  * On Linux: `rm -R /tmp/winstoneEmbeddedWAR`
  * On Windows: remove the `winstoneEmbeddedWAR` that should be somewhere under `C:\Users\<username>\AppData\Local\Temp`

## Optional configuration ##

Alternatively, you can start WebAnno from the command line, in particular if you wish to provide it with additional memory (here 1 GB) or if you want it to store its data in a different folder.
```
java -Xmx1g -Dwebanno.home=/my/webanno/home -jar webanno-XXX-standalone.jar
```

Mind to replace `/my/webanno/home` with path of a folder where WebAnno can store its data.

By default the server starts on port 8080 an you can access it via a browser at http://localhost:8080 after you started it. You can add the parameter `--httpPort=9999` at the end of the command line to start the server on port 9999 (or choose any other port).

A full list of the command line parameters can be found here: http://winstone.sourceforge.net/#commandLine