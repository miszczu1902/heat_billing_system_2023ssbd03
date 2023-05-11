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

const token =
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjgzODM1NzczLCJyb2xlIjoiQURNSU4iLCJleHAiOjE2ODM4Mzc1NzN9.jN5Rz3Hi3iR7ABheZf0VxMgUjE4LipupCPiTxOdwr5s';

const AccountsList = () => {
    const navigate = useNavigate();

    const [accounts, setAccounts] = useState<AccountFromList[]>([]);
    const [pageNumber, setPageNumber] = useState(0);
    const [size, setSize] = useState(10);
    const [sortBy, setSortBy] = useState('username');
    const [total, setTotal] = useState<number>(0);

    useEffect(() => {
        const fetchData = async () => {
            const response = await axios.get(`${API_URL}/accounts?sortBy=${sortBy}&pageNumber=${pageNumber}`, {
                headers: {
                    Authorization: 'Bearer ' + token
                }
            });
            setAccounts(response.data);
        };

        fetchData();
    }, [sortBy, pageNumber]);

    const handleChangePage = (event: React.MouseEvent<HTMLButtonElement> | null, newPage: number) => {
        setPageNumber(newPage);
    };

    const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setSize(parseInt(event.target.value, 10));
        setPageNumber(0);
    };

    const handleSort = (column: string) => {
        setSortBy(column);
    };

    const goToAccount = (username:string) =>  {
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
                    </TableRow>
                </TableHead>
                <TableBody>
                    {accounts.map((accounts) => (
                        <TableRow key={accounts.id} onClick={() => goToAccount(accounts.username)}>
                            <TableCell component='th' scope='row'/>
                            <TableCell>{accounts.username}</TableCell>
                            <TableCell>{accounts.email}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <TablePagination count={size} page={pageNumber} rowsPerPage={10}
                             onPageChange={handleChangePage}></TablePagination>
        </TableContainer>
    );
}

export default AccountsList;