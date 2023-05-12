import {createBrowserRouter, Outlet} from "react-router-dom";
import NavbarPanel from "../components/navigation/NavbarPanel";
import EditPersonalData from "../components/personalData/EditPersonalData";

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
                path: "/accounts/self/personal-data",
                element: <EditPersonalData/>
            }
        ]
    }
]);

export default router;