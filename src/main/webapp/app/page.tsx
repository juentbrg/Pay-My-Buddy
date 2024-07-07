"use client"

import withAuth from "../components/withAuth";
import React, {useEffect, useState} from "react";
import Navigation from "../components/Navigation";
import Transfert from "../components/Transfert"
import TransactionContainer from "@/components/TransactionContainer";
import axios from "axios";

interface TransactionObj {
    relationName: string
    description: string
    amount: number
}

const Home = () => {
    const [transactions, setTransactions] = useState<TransactionObj[]>([])

    const handleGetTransactions = async () => {
        try {
            const response = await axios.get("http://localhost:8080/api/transaction/find-all", {withCredentials: true})

            if (response.status === 200) {
                setTransactions(response.data)
            }
        } catch (error) {
            console.error("Error fetching transactions:", error);
        }
    }

    useEffect(() => {
        handleGetTransactions();
    }, []);

  return (
    <>
        <Navigation />
        <main>
          <Transfert onTransactionCreated={handleGetTransactions}/>
            <TransactionContainer transactions={transactions}/>
        </main>
    </>
  );
}

export default withAuth(Home);
