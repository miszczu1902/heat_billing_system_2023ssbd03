import React from "react";
import {useState, useEffect} from "react";
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    TablePagination,
} from "@mui/material";
import {makeStyles} from '@mui/styles'
import {AccountFromList} from "../../types/accountFromList";
import axios from "axios";

const GET_ACCOUNTS_LIST_URL = 'https://localhost/api/accounts';
const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjgzNzQ0NDA3LCJyb2xlIjoiQURNSU4iLCJleHAiOjE2ODM3NDYyMDd9.6tfaMeggq7HTjoCIe1ubWCPP6XVEsNnhLpuRiv4wT5I';

const AccountsList = () => {
    const classes =  makeStyles({
        table: {
            minWidth: 650,
        },
    });

    const [accounts, setAccounts] = useState<AccountFromList[]>([]);
    const [pageNumber, setPageNumber] = useState(0);
    const [size, setSize] = useState(10);
    const [sortBy, setSortBy] = useState("username");
    const [total, setTotal] = useState<number>(0);

    useEffect(() => {
        axios.get(GET_ACCOUNTS_LIST_URL + '?sortBy=' + sortBy + '&pageNumber=' + pageNumber, {
            headers: {
                Authorization: 'Bearer ' + token
            }
        })
            .then(response => {
                setAccounts(response.data);
                setTotal(accounts.length);
            })
            .catch(error => {
                // handle error
            });
    }, [pageNumber, size]);

    const handleChangePage = (event: React.MouseEvent<HTMLButtonElement> | null, newPage: number) => {
        setPageNumber(newPage);
    };

    const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setSize(parseInt(event.target.value, 10));
        setPageNumber(0);
    };

    return (
        <TableContainer component={Paper}>
            <Table aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell/>
                        <TableCell>Username</TableCell>
                        <TableCell>Email</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {accounts.map((accounts) => (
                        <TableRow key={accounts.id}>
                            <TableCell component="th" scope="row"/>
                            <TableCell>{accounts.username}</TableCell>
                            <TableCell>{accounts.email}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <TablePagination count={size} page={pageNumber} rowsPerPage={10} onPageChange={handleChangePage}></TablePagination>
        </TableContainer>
    );
}

export default AccountsList;