"use client"

import Navigation from "../../components/Navigation";
import withAuth from "@/components/withAuth";
import {useState} from "react";
import axios from "axios";

const Relation = () => {
    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("");

    const handleSearchRelation = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const response = await axios.post("http://localhost:8080/api/connection/add-connection", {
            email
        }, {
            withCredentials: true
        })

        if (response.status === 200) {
            setEmail("")
            setMessage("Relation ajoutée avec succès")
        }
    }

    return (
        <>
            <Navigation />
            <main className={"flex justify-center items-center"}>
                    <form className={"flex items-center justify-center w-[950px] h-[330px] mt-[150px]"} onSubmit={handleSearchRelation}>
                        <div className={"flex flex-col"}>
                            <label className={"mb-2"} htmlFor={"relationEmail"}>Chercher une relation</label>
                            <input
                                className={"w-[484px] h-[54px] outline-none border-[1px] border-gray-300 rounded-lg p-6"}
                                type={"email"}
                                id={"relationEmail"}
                                name={"email"}
                                placeholder={"Saisir une adresse email"}
                                value={email}
                                onChange={(e => setEmail(e.target.value))}
                            />
                            {message && <p className={"text-green-500 mt-2"}>{message}</p>}
                        </div>
                        <button className={"w-[151px] h-[62px] bg-[#F69F1D] rounded-lg text-white text-lg font-bold ml-4 transition-bg duration-200 ease-in-out hover:bg-[#f7ad3f]"} type={"submit"}>Ajouter</button>
                    </form>
            </main>
        </>
    )
}

export default withAuth(Relation);
