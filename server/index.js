"use strict";

const youtubeScraper = require("scrape-youtube").default;
const ytdl = require("youtube-dl");
const express = require("express");

const app = express();
const PORT = 3000;

app.get("/api/youtube/search", async (request, response) => {
  const query = request.query.query;
  if(!query) {
    const statusCode = 422;
    const errorJSON = {
      error_code: statusCode,
      error_message: "Illegal value of query argument"
    }
    response.status(statusCode).json(errorJSON);
  } else {
    const searchResults = await getSearchResults(query);
    response.status(200).json(searchResults);
  }
});

async function getSearchResults(query) {
  const searchResults = await searchYouTube(query);
  const searchResultsWithInfo =
    await Promise.all(
      searchResults.map(result => getInfo(result))
        //this filter removes any null, undefined objects from the array
        .filter(x => x)
    );

  return {
    items: searchResultsWithInfo
  }
}

async function searchYouTube(query) {
  return youtubeScraper.search(query)
}

async function getInfo(searchResult) {
  return new Promise(((resolve, reject) => {
    ytdl.getInfo(searchResult.link, (error, info) => {
      if (error) {
        console.error("Failed to extract info for link = " + searchResult.link, error);
        return resolve(null);
      } else {
        const infoJson = {
          video_id: info.id,
          video_title: info.title,
          video_thumbnail_url: info.thumbnail,
          video_stream_url: info.url
        }
        return resolve(infoJson);
      }
    });
  }));
}

app.listen(PORT, () => {
  console.log("RatherTube server running at http://localhost:" + PORT);
});
