import React, {useEffect, useState} from 'react';
import {Button, Container, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from '@mui/material';
import {AccountFromList} from '../../types/accountFromList';
import axios from 'axios';
import {API_URL} from '../../consts';
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import RefreshIcon from "../icons/RefreshIcon";

const AccountsList = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const token = 'Bearer ' + localStorage.getItem("token");
    const [accounts, setAccounts] = useState<AccountFromList[]>([]);
    const [pageNumber, setPageNumber] = useState(0);
    const [size, setSize] = useState(1);
    const [sortBy, setSortBy] = useState('username');
    const [total, setTotal] = useState<number>(0);

    const fetchData = async () => {
        axios.get(`${API_URL}/accounts?sortBy=${sortBy}`, {
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
    }, [sortBy]);

    const handleChangePage = (event: React.MouseEvent<HTMLButtonElement> | null, newPage: number) => {
        setPageNumber(newPage);
    };

    const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setSize(parseInt(event.target.value, size));
        setPageNumber(pageNumber);
    };

    const handleClick = () => {
        fetchData();
        navigate('/accounts');
    };

    const handleSort = (column: string) => {
        setSortBy(column);
    };

    const goToAccount = (username: string) => {
        navigate('/accounts/' + username);
    }

    return (
        <TableContainer component={Paper} sx={{display: 'flex',
            flexDirection: 'column',
            overflowY: 'auto',
            overflow: 'hidden',
            maxHeight: '90vh'}}>
            <Table aria-label='simple table'>
                <TableHead>
                    <TableRow>
                        <TableCell><Button className="landing-page-button" onClick={handleClick}><RefreshIcon/></Button></TableCell>
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
                        <TableRow key={accounts.id}>
                            <TableCell component='th' scope='row'/>
                            <TableCell onClick={() => goToAccount(accounts.username)}
                                       sx={{cursor: 'pointer'}}>{accounts.username}</TableCell>
                            <TableCell>{accounts.email}</TableCell>
                            {accounts.isEnable ? <TableCell>{t('account_list.active')}</TableCell> :
                                <TableCell>{t('account_list.inactive')}</TableCell>}
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