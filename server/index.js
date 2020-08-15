"use strict";

const youtubeScraper = require("scrape-youtube").default;
const ytdl = require("youtube-dl");
const express = require("express");

const app = express();
const PORT = 3000;

//region GET search API
app.get("/api/youtube/search", async (request, response) => {
  const query = request.query.query;
  if (!query) {
    const statusCode = 422;
    const errorJSON = {
      error_code: statusCode,
      error_message: "Illegal value of query argument"
    }
    response.status(statusCode).json(errorJSON);
  } else {
    const searchResults = await getSearchResults(query);
    const responseJson = {
      items: searchResults
    }
    response.status(200).json(responseJson);
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
app.get("/api/youtube/stream", async (request, response) => {
  const videoId = request.query.videoId;
  if (!videoId) {
    const statusCode = 422;
    const errorJSON = {
      error_code: statusCode,
      error_message: "Illegal value of videoId argument"
    }
    response.status(statusCode).json(errorJSON);
  } else {
    const streamUrl = await getStreamUrl(videoId);
    const responseJSON = {
      stream_url: streamUrl
    };
    response.status(200).json(responseJSON);
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

app.listen(PORT, () => {
  console.log("RatherTube server running at http://localhost:" + PORT);
});
