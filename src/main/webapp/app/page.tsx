"use client"

import withAuth from "../components/withAuth";
import React from "react";
import Navigation from "../components/Navigation";

const Home = () => {
  return (
    <>
        <Navigation />
        <main>
          <h1>Home</h1>
        </main>
    </>
  );
}

export default withAuth(Home);
