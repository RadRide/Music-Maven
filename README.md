
# Music Maven

Music Maven is an application that utilizes the spotdl python library by [xnetcat](https://github.com/xnetcat) to download songsfrom Spotify, Youtube Music, etc. You can queue multiple songs to retrieve their information and then download them all to a desired location. This application is still in early development so if any errors occur, please let me know.


## Acknoledgements 

 - [xnetcat](https://github.com/xnetcat)
 - [Spotdl](https://github.com/spotDL/spotify-downloader)


## Demo

A Demo is included in the project to test out. To run it please see [Installation](#installation)


## Installation

Before running this project, you need to install python, the install spotdl by running the following command in you cmd or powershell

```bash
  pip install spotdl
```
Then you need to install ffmpeg by running the following command

```bash
  spotdl --download-ffmpeg
```
Or you can get it from the demo file that comes with the jar of the application. After that you need to add extract it in the C:\Windows directory then add C:\Windows\ffmpeg\bin to you local variables PATH.

After that you should be able to run the application without any issues.