import type { ChangeEvent } from "react";
import { useNavigate } from 'react-router-dom';

import { useLibrary } from "../ctx/Authctx"
import { User, type UserStatus } from "../model/User";
export const Auth = () => {
    const navigate = useNavigate();
    const ctx = useLibrary();
    const handleChange = (e: ChangeEvent<HTMLSelectElement>) => {
        const role = e.target.value as UserStatus;
        ctx.setCurrentUser(new User(ctx.currentUser.id + 1, "John", "Smith", "a@p.pl", role))
        role === "admin" ? navigate("/admin") : navigate("/books");
    }
    return (
        <form>
            <span>change user</span>

            {ctx.currentUser.status === "admin" ?
                <select onChange={handleChange}>
                    <option value="admin">admin</option>
                    <option value="user">user</option>
                </select>
                :
                <select onChange={handleChange}>
                    <option value="user">user</option>
                    <option value="admin">admin</option>
                </select>
            }
        </form>
    )
}