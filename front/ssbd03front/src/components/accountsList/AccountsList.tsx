import React from 'react';
import {useState, useEffect} from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    TablePagination
} from '@mui/material';
import {AccountFromList} from '../../types/accountFromList';
import axios from 'axios';
import {API_URL} from '../../consts';
import {useNavigate} from "react-router-dom";
import {useCookies} from "react-cookie";

const AccountsList = () => {
    const navigate = useNavigate();
    const [cookies, setCookie] = useCookies(['token']);
    const token = 'Bearer ' + cookies.token;
    const [accounts, setAccounts] = useState<AccountFromList[]>([]);
    const [pageNumber, setPageNumber] = useState(0);
    const [size, setSize] = useState(10);
    const [sortBy, setSortBy] = useState('username');
    const [total, setTotal] = useState<number>(0);

    const fetchData = async () => {
        axios.get(`${API_URL}/accounts?sortBy=${sortBy}&pageNumber=${pageNumber}&pageSize=${size}`, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setAccounts(response.data);
            setTotal(response.data.length);
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
        // console.log(sortBy, pageNumber, size);
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
                            Username
                        </TableCell>
                        <TableCell onClick={() => handleSort('email')}>
                            Email
                        </TableCell>
                        <TableCell onClick={() => handleSort('email')}>
                            Active status
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {accounts.map((accounts) => (
                        <TableRow key={accounts.id} onClick={() => goToAccount(accounts.username)}>
                            <TableCell component='th' scope='row'/>
                            <TableCell>{accounts.username}</TableCell>
                            <TableCell>{accounts.email}</TableCell>
                            {accounts.isEnable ? <TableCell>Aktywny</TableCell> : <TableCell>Nieaktywny</TableCell>}
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <TablePagination rowsPerPageOptions={[1, 5, 10, 25]}
                             count={total} page={pageNumber} rowsPerPage={size}
                             onPageChange={handleChangePage} onRowsPerPageChange={handleChangeRowsPerPage}
                             labelRowsPerPage="Wierszy na stronę"
                             labelDisplayedRows={({from, to, count}) => `${from}-${to} z ${count}`}></TablePagination>
        </TableContainer>
    );
}

export default AccountsList;