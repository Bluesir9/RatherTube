"use strict";

const youtubeScraper = require("scrape-youtube").default;
const ytdl = require("youtube-dl");
const express = require("express");
const cors = require("cors");

const app = express();
app.use(cors({
  origin: process.env.client_origin
}));
const PORT = 3000;

//region GET search API
app.get("/api/youtube/search", async (request, response, next) => {
  const query = request.query.query;
  if (!query) {
    const statusCode = 422;
    const errorJSON = {
      error_code: statusCode,
      error_message: "Illegal value of query argument"
    }
    response.status(statusCode).json(errorJSON);
  } else {
    try {
      const searchResults = await getSearchResults(query);
      const responseJson = {
        items: searchResults
      }
      response.status(200).json(responseJson);
    } catch (error) {
      next(error);
    }
  }
});

async function getSearchResults(query) {
  const searchResults = await searchYouTube(query);
  return searchResults.map(function (result) {
      return {
        video_id: result.id,
        video_title: result.title,
        video_artist: result.channel.name,
        video_thumbnail: result.thumbnail,
      }
    });
}

async function searchYouTube(query) {
  return youtubeScraper.search(query);
}

//endregion

//region GET stream API
app.get("/api/youtube/stream", async (request, response, next) => {
  const videoId = request.query.videoId;
  if (!videoId) {
    const statusCode = 422;
    const errorJSON = {
      error_code: statusCode,
      error_message: "Illegal value of videoId argument"
    }
    response.status(statusCode).json(errorJSON);
  } else {
    try {
      const streamUrl = await getStreamUrl(videoId);
      const responseJSON = {
        stream_url: streamUrl
      };
      response.status(200).json(responseJSON);
    } catch (error) {
      next(error)
    }
  }
});

async function getStreamUrl(videoId) {
  const videoUrl = "https://www.youtube.com/watch?v=" + videoId;
  return new Promise(((resolve) => {
    ytdl.getInfo(videoUrl, (error, info) => {
      if (error) {
        console.error("Failed to extract info for link = " + searchResult.link, error);
        return resolve(null);
      } else {
        return resolve(info.url);
      }
    });
  }));
}
//endregion

//region 404 route
/*
Note:
  This should always be the "last" route
  attached. This way, its ensured that after
  none of the routes attached prior to it are
  matched, this route is reached and a 404
  response is returned.
 */
app.use((request, response, next) => {
  const errorJson = {
    error_code: 404,
    error_message: "404, route not found"
  }
  response.status(404).json(errorJson);
});
//endregion

//region Error handler
app.use((error, request, response, next) => {
  console.error(error);
  const errorJson = {
    error_code: 500,
    error_message: "Internal server error"
  }
  response.status(500).json(errorJson);
});
//endregion

app.listen(PORT, () => {
  console.log("RatherTube server running at http://localhost:" + PORT);
});
