"use client"

import Navigation from "../../components/Navigation";
import withAuth from "@/components/withAuth";
import caretRight from "../../assets/icons/caret-right.svg"
import Image from "next/image";
import React, {useState} from "react";
import axios from "axios";

const Profile = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");

    const handleChangeUserData = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const response = await axios.put("http://localhost:8080/api/user/update", {
                username,
                email,
                password
            }, {
                withCredentials: true
            });

            if(response.status === 200) {
                setUsername("")
                setEmail("")
                setPassword("")
                setMessage("Informations correctement modifiés")
            }
        } catch (e) {
            console.error(e)
        }
    }

    return (
        <>
            <Navigation />
            <main className={"flex justify-center items-center"}>
                <form className={"flex justify-center w-[750px] flex-row p-12 pt-36"} onSubmit={handleChangeUserData}>
                    <div className={"h-[150px]"}>
                        <div className={"flex flex-row w-[350px] text-lg"}>
                            <label htmlFor={"username"}><strong>Username</strong></label>
                            <input
                                className={"w-[112px] outline-none ml-10 text-lg"}
                                type={"text"}
                                id={"username"}
                                placeholder={"@Username"}
                                name={"Username"}
                                value={username}
                                required={false}
                                onChange={(e => setUsername(e.target.value))}
                            />
                            <Image className={"ml-4"} src={caretRight} alt={"caret right"}/>
                        </div>
                        <div className={"flex flex-row w-[350px] text-lg mt-4"}>
                            <label htmlFor={"mail"}><strong>Mail</strong></label>
                            <input
                                className={"w-[177px] outline-none ml-[100px] text-lg"}
                                type={"email"}
                                id={"mail"}
                                placeholder={"nom@domain.com"}
                                name={"Mail"}
                                value={email}
                                required={false}
                                onChange={(e => setEmail(e.target.value))}
                            />
                            <Image className={"ml-4"} src={caretRight} alt={"caret right"}/>
                        </div>
                        <div className={"flex flex-row w-[350px] text-lg mt-4"}>
                            <label htmlFor={"password"}><strong>Password</strong></label>
                            <input
                                className={"w-[87px] outline-none ml-[47px] text-lg"}
                                type={"password"}
                                id={"password"}
                                placeholder={"password"}
                                name={"Password"}
                                value={password}
                                required={false}
                                onChange={(e => setPassword(e.target.value))}
                            />
                            <Image className={"ml-4"} src={caretRight} alt={"caret right"}/>
                        </div>
                        {message && <p className={"text-green-500 mt-2"}>{message}</p>}
                    </div>
                    <button className={"w-[147px] h-[64px] bg-[#F69F1D] rounded-lg text-white text-lg font-bold mt-20 ml-8 transition-bg duration-200 ease-in-out hover:bg-[#f7ad3f]"} type={"submit"}>Modifier</button>
                </form>
            </main>
        </>
    )
}

export default withAuth(Profile);
