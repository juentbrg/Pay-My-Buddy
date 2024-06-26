"use client"

import withAuth from "../components/withAuth";
import React from "react";
import Navigation from "../components/Navigation";
import Transfert from "../components/Transfert"
import TransactionContainer from "@/components/TransactionContainer";

const Home = () => {
  return (
    <>
        <Navigation />
        <main>
          <Transfert />
            <TransactionContainer />
        </main>
    </>
  );
}

export default withAuth(Home);
