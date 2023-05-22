import React, {useEffect, useState} from 'react';
import {
    Container,
    Grid,
    Pagination,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from '@mui/material';
import {AccountFromList} from '../../types/accountFromList';
import axios from 'axios';
import {API_URL} from '../../consts';
import {useNavigate} from "react-router-dom";
import {useCookies} from "react-cookie";
import {useTranslation} from "react-i18next";

const AccountsList = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const [cookies, setCookie] = useCookies(['token', 'role']);
    const token = 'Bearer ' + cookies.token;
    const [accounts, setAccounts] = useState<AccountFromList[]>([]);
    const [pageNumber, setPageNumber] = useState(0);
    const [size, setSize] = useState(1);
    const [sortBy, setSortBy] = useState('username');
    const [total, setTotal] = useState<number>(0);
    const [role, setRole] = useState(cookies.role);

    const fetchData = async () => {
        axios.get(`${API_URL}/accounts?sortBy=${sortBy}&pageNumber=${pageNumber}&pageSize=${size}`, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setAccounts(response.data);
            setTotal(response.data.length);
        }).catch(error => {
            if (error.response.status == 403) navigate('/');
        })
    };

    useEffect(() => {
        fetchData();
    }, [sortBy, pageNumber, size]);

    const handleChangePage = (event: React.MouseEvent<HTMLButtonElement> | null, newPage: number) => {
        setPageNumber(newPage);
    };

    const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setSize(parseInt(event.target.value, size));
        setPageNumber(pageNumber);
    };

    const handleSort = (column: string) => {
        setSortBy(column);
    };

    const goToAccount = (username: string) => {
        navigate('/accounts/' + username);
    }

    return (
            <TableContainer component={Paper}>
                <Table aria-label='simple table'>
                    <TableHead>
                        <TableRow>
                            <TableCell/>
                            <TableCell onClick={() => handleSort('username')}>
                                {t('login.username')}
                            </TableCell>
                            <TableCell onClick={() => handleSort('email')}>
                                {t('register.email')}
                            </TableCell>
                            <TableCell>
                                {t('account_list.active_status')}
                            </TableCell>
                            <TableCell>
                                {t('account_list.confirmation_status')}
                            </TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {accounts.map((accounts) => (
                            <TableRow key={accounts.id} >
                                <TableCell component='th' scope='row'/>
                                <TableCell onClick={() => goToAccount(accounts.username)} sx={{cursor: 'pointer'}}>{accounts.username}</TableCell>
                                <TableCell>{accounts.email}</TableCell>
                                {accounts.isEnable ? <TableCell>{t('account_list.active')}</TableCell> : <TableCell>{t('account_list.inactive')}</TableCell>}
                                {accounts.isActive ? <TableCell>{t('account_list.confirmed')}</TableCell> :
                                    <TableCell>{t('account_list.unconfirmed')}</TableCell>}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
    );
}

export default AccountsList;