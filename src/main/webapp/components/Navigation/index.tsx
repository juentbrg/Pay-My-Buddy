"use client"

import Link from "next/link";
import {usePathname, useRouter} from "next/navigation";
import axios from "axios";

const Navigation = () => {
    const pathname = usePathname();
    const router = useRouter();

    const handleLogout = async() => {
        try {
            const response = await axios.post("http://localhost:8080/api/auth/logout", {}, {
                withCredentials: true
            });
            if (response.status === 200){
                router.push("/login");
            }
        } catch (error:any) {
            console.error(error)
        }
    }

    return (
        <nav className={"flex flex-row items-center justify-between h-[164px] p-20 text-[20px] font-[500]"}>
            <h1>Pay My Buddy</h1>
            <ul className="flex flex-row">
                <li className={`mr-10 transition-colors duration-300 ease-in-out hover:text-[#207FEE] ${pathname === '/' ? 'text-[#207FEE]' : ''}`}>
                    <Link href="/">Transférer</Link>
                </li>
                <li className={`mr-10 transition-colors duration-300 ease-in-out hover:text-[#207FEE] ${pathname === '/profile' ? 'text-[#207FEE]' : ''}`}>
                    <Link href="/profile">Profil</Link>
                </li>
                <li className={`mr-10 transition-colors duration-300 ease-in-out hover:text-[#207FEE] ${pathname === '/relation' ? 'text-[#207FEE]' : ''}`}>
                    <Link href="/relation">Ajouter relation</Link>
                </li>
                <li className="mr-10 transition-colors duration-300 ease-in-out hover:text-[#207FEE]">
                    <button onClick={handleLogout}>Se déconnecter</button>
                </li>
            </ul>
        </nav>
    )
}

export default Navigation;
