import {createBrowserRouter, Outlet} from 'react-router-dom';
import NavbarPanel from '../components/navigation/NavbarPanel';
import EditPersonalData from '../components/personalData/EditPersonalData';
import Login from '../components/login/Login';
import EnableAccount from '../components/accounts/EnableAccount';
import DisableAccount from '../components/accounts/DisableAccount';
import EditUserPersonalData from '../components/personalData/EditUserPersonalData';
import LandingPage from '../components/landingPage/LandingPage';
import AccountsList from '../components/accountsList/AccountsList';
import Registration from "../components/registration/Registration";
import EditPassword from "../components/passwords/EditPassword";
import ResetPassword from "../components/passwords/ResetPassword";
import ActivateFromEmail from "../components/registration/ActivateFromEmail";
import ChangePhoneNumber from "../components/owner/ChangePhoneNumber";
import EditUserPassword from "../components/passwords/EditUserPassword";
import Profile from "../components/account/Profile";
import SelfProfile from "../components/account/SelfProfile";
import EditEmail from "../components/email/EditEmail";
import ConfirmEmail from "../components/email/ConfirmEmail";
import EditUserEmail from "../components/email/EditUserEmail";
import OwnerProfile from "../components/account/OwnerProfile";
import ManagerProfile from "../components/account/ManagerProfile";
import AdminProfile from "../components/account/AdminProfile";

const router = createBrowserRouter([
    {
        path: '/',
        element: (<><Outlet/></>),
        children: [
            {
                path: '/',
                element: <LandingPage/>
            },
            {
                path: '/accounts',
                element: (<><NavbarPanel/><Outlet/></>),
                children: [
                    {
                        path: '/accounts',
                        element: <AccountsList/>
                    },
                    {
                        path: '/accounts/self',
                        element: <SelfProfile/>
                    },
                    {
                        path: '/accounts/self/owner',
                        element: <OwnerProfile/>
                    },
                    {
                        path: '/accounts/self/manager',
                        element: <ManagerProfile/>
                    },
                    {
                        path: '/accounts/self/admin',
                        element: <AdminProfile/>
                    },
                    {
                        path: '/accounts/:username',
                        element: <Profile/>
                    },
                    {
                        path: '/accounts/self/personal-data',
                        element: <EditPersonalData/>
                    },
                    {
                        path: '/accounts/:username/personal-data',
                        element: <EditUserPersonalData/>
                    },
                    {
                        path: '/accounts/:username/email',
                        element: <EditUserEmail/>
                    },
                    {
                        path: '/accounts/:username/enable',
                        element: <EnableAccount/>
                    },
                    {
                        path: '/accounts/:username/disable',
                        element: <DisableAccount/>
                    },
                ]
            },
            {
                path: "/accounts/self/password",
                element: <EditPassword/>
            },
            {
                path: "/accounts/reset-password",
                element: <ResetPassword/>
            },
            {
                path: "/accounts/:username/password",
                element: <EditUserPassword/>
            },
            {
                path: '/login',
                element: <Login/>
            },
            {
                path: '/register',
                element: <Registration/>
            },
            {
                path: '/activate-from-email/:activationToken',
                element: <ActivateFromEmail/>
            },
            {
                path: "/accounts/self/phone-number",
                element: <ChangePhoneNumber/>
            },
            {
                path: '/accounts/self/email',
                element: <EditEmail/>
            },
            {
                path: '/accounts/self/confirm-new-email/:activationToken',
                element: <ConfirmEmail/>
            }
        ]
    }
]);
export default router;