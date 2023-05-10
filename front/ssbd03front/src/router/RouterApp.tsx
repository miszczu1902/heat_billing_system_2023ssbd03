import {createBrowserRouter, Outlet} from 'react-router-dom';
import EditUserPersonalData from '../components/personalData/EditUserPersonalData';
import NavbarPanel from '../components/navigation/NavbarPanel';
import AccountsList from '../components/accountsList/AccountsList';
import EditPersonalData from '../components/personalData/EditPersonalData';

const router = createBrowserRouter([
    {
        path: '/',
        element: (
            <>
                <NavbarPanel/>
                <Outlet/>
            </>
        ),
        children: [
            {
                path: '/accounts',
                element: <AccountsList/>
            },
            {
                path: '/accounts/:username',
                element: <EditPersonalData/>
            }
        ]
    },
    {
        path: '/self',
        element: <EditUserPersonalData/>
    }
]);

export default router;