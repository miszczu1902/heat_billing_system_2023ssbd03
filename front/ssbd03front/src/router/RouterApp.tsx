import {createBrowserRouter, Outlet, useParams} from "react-router-dom";
import NavbarPanel from "../components/navigation/NavbarPanel";
import EditUserPersonalData from "../components/personalData/EditUserPersonalData";

const router = createBrowserRouter([
    {
        path: "/",
        element: (
            <>
                <NavbarPanel/>
                <Outlet/>
            </>
        ),
        children: [
            {
                path: "/#",
            },

            {
                path: "/accounts",
            },
            {
                path: "/accounts/self",
            },
            {
                path: "/accounts/self/personal-data",
            }, 
            {
                path: "/accounts/:username/personal-data",
                element: <EditUserPersonalData/>
            }
        ]
    }
]);

export default router;