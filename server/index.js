"use strict";

require("dotenv");
const youtubeScraper = require("scrape-youtube").default;
const ytdl = require("youtube-dl");
const express = require("express");
const cors = require("cors");
const moment = require("moment");

const config = {
  clientOrigin: process.env.CLIENT_ORIGIN,
  expressPort: process.env.PORT || process.env.EXPRESS_PORT
}

const app = express();
app.use(cors({
  origin: config.clientOrigin
}));
const PORT = config.expressPort;

//region GET search API
app.get("/api/youtube/search", async (request, response, next) => {
  try {
    const query = request.query.query;
    if (!query) {
      const errorJson = {
        apiErrorCode: 422,
        apiErrorMessage: "Illegal value of query argument"
      }
      next(errorJson);
    } else {
      const searchResults = await getSearchResults(query);
      const responseJson = {
        items: searchResults
      }
      response.status(200).json(responseJson);
    }
  } catch (error) {
    next(error);
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
        video_length_millis: moment.duration(result.duration, "seconds").asMilliseconds()
      }
    });
}

async function searchYouTube(query) {
  return youtubeScraper.search(query)
      .then(results => results.videos);
}

//endregion

//region GET stream API
app.get("/api/youtube/stream", async (request, response, next) => {
  try {
    const videoId = request.query.videoId;
    if (!videoId) {
      const errorJson = {
        apiErrorCode: 422,
        apiErrorMessage: "Illegal value of videoId argument"
      }
      next(errorJson);
    } else {
      const streamUrl = await getStreamUrl(videoId);
      const responseJSON = {
        stream_url: streamUrl
      };
      response.status(200).json(responseJSON);
    }
  } catch (error) {
    next(error);
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
  let statusCode = error.apiErrorCode || 500;
  let message = error.apiErrorMessage || "Internal server error";
  const errorJson = {
    error_code: statusCode,
    error_message: message
  }
  response.status(statusCode).json(errorJson);
});
//endregion

app.listen(PORT, () => {
  console.log("RatherTube server running");
});
