from flask import Flask
from youtube_caption_client import YoutubeCaptionClient

app = Flask(__name__)

@app.route('/')
def hello():
    return 'Hello, World!'

@app.route('/rest/captions/<videoId>')
def getYoutubeCaption(videoId):
    print(videoId)
    transcript = YoutubeCaptionClient.get_transcript(videoId)
    return transcript

if __name__ == '__main__':
    app.run(debug=True)
