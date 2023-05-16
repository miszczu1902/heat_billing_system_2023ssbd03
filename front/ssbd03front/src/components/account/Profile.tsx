import React, {useState} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import TextField from '@mui/material/TextField';
import {useParams} from "react-router-dom";
import {API_URL} from "../../consts";
import {useCookies} from "react-cookie";
import axios from 'axios';
import Box from "@mui/material/Box";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";

const roles = [
    {value: "admin", label: "Administrator"},
    {value: "manager", label: "Manager"},
    {value: "owner", label: "Właściciel"}
];

export default function Profile() {
    const [cookies] = useCookies(["token"]);
    const token = "Bearer " + cookies.token;

    const [license, setLicense] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const username = useParams().username;

    const [phoneNumberError, setPhoneNumberError] = React.useState("");
    const [licenseError, setLicenseError] = React.useState("");
    const [phoneNumberValid, setPhoneNumberValid] = React.useState(false);
    const [licenseValid, setLicenseValid] = React.useState(false);

    const [confirmOpen, setConfirmOpen] = React.useState(false);
    const [successOpen, setSuccessOpen] = React.useState(false);

    const [errorOpen, setErrorOpen] = React.useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = React.useState("");
    const [dataError, setDataError] = React.useState("");

    const [isAdmin, setIsAdmin] = React.useState(false);
    const [isOwner, setIsOwner] = React.useState(false);
    const [isManager, setIsManager] = React.useState(false);

    const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
        if (isManager && !isOwner && !isAdmin) {
            const addAccessLevelManagerDTO = {
                username: username,
                license: license.toString()
            }

            axios.patch(`${API_URL}/accounts/add-access-level-manager`,
                addAccessLevelManagerDTO, {
                    headers: {
                        'Authorization': token,
                        'Content-Type': 'application/json'
                    },
                })
                .then(response => {
                    setSuccessOpen(true);
                })
                .catch(error => {
                    setErrorOpenMessage(error.response.data.message)
                    setErrorOpen(true);
                });
            handleClose(event, reason);
        }
        if (isOwner && !isManager && !isAdmin) {
            const addAccessLevelOwnerDTO = {
                username: username,
                phoneNumber: phoneNumber.toString()
            }

            axios.patch(`${API_URL}/accounts/add-access-level-owner`,
                addAccessLevelOwnerDTO, {
                    headers: {
                        'Authorization': token,
                        'Content-Type': 'application/json'
                    },
                })
                .then(response => {
                    setSuccessOpen(true);
                })
                .catch(error => {
                    setErrorOpenMessage(error.response.data.message)
                    setErrorOpen(true);
                });
            handleClose(event, reason);
        }
        if (isAdmin && !isOwner && !isManager) {
            const addAccessLevelAdminDTO = {
                username: username
            }

            axios.patch(`${API_URL}/accounts/add-access-level-admin`,
                addAccessLevelAdminDTO, {
                    headers: {
                        'Authorization': token,
                        'Content-Type': 'application/json'
                    },
                })
                .then(response => {
                    setSuccessOpen(true);
                })
                .catch(error => {
                    setErrorOpenMessage(error.response.data.message)
                    setErrorOpen(true);
                });
            handleClose(event, reason);
        }
    }

    const handleClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setIsManager(false);
            setIsOwner(false);
            setIsAdmin(false);
        }
    };

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpen(false);
            setConfirmOpen(false);
            setIsManager(false);
            setIsOwner(false);
            setIsAdmin(false);
        }
    };

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        setLicense("");
        setPhoneNumber("");
        setIsManager(false);
        setIsOwner(false);
        setIsAdmin(false);
        if (reason !== 'backdropClick') {
            setSuccessOpen(false);
        }
    }

    const handleConfirm = () => {
        if (licenseValid || phoneNumberValid) {
            setDataError("");
            setConfirmOpen(true);
        } else {
            setDataError("Wprowadź poprawne dane");
        }
    }

    const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
    }

    const handlePhoneNumber = (event: React.ChangeEvent<HTMLInputElement>) => {
        let phoneNumber = event.target.value;
        setPhoneNumber(phoneNumber);
        const regex = /^\d{9}$/;
        if (!regex.test(phoneNumber)) {
            setPhoneNumberError("Numer telefonu musi zawierać 9 cyfr.");
            setPhoneNumberValid(false);
        } else {
            setPhoneNumberError("");
            setPhoneNumberValid(true);
        }
    };

    const handleLicense = (event: React.ChangeEvent<HTMLInputElement>) => {
        let license = event.target.value;
        setLicense(license);
        const regex = /^.{20}$/;
        if (!regex.test(license)) {
            setLicenseError("Licencja musi skladać się z 20 znaków.");
            setLicenseValid(false);
        } else {
            setLicenseError("");
            setLicenseValid(true);
        }
    };

    const handleClickOpenManager = () => {
        setIsManager(true);
    };

    const handleClickOpenOwner = () => {
        setIsOwner(true);
    };

    const handleClickOpenAdmin = () => {
        setIsAdmin(true);
    };

    return (
        <div>
            <Button onClick={handleClickOpenManager} variant="contained">
                Dodaj poziom dostępu manager
            </Button>
            <Button onClick={handleClickOpenOwner} variant="contained">
                Dodaj poziom dostępu owner
            </Button>
            <Button onClick={handleClickOpenAdmin} variant="contained">
                Dodaj poziom dostępu admin
            </Button>

            <Dialog disableEscapeKeyDown open={isManager} onClose={handleClose}>
                <DialogTitle>Wypełnij formularz dodania uprawnien zarządcy</DialogTitle>
                <DialogContent>
                    <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                        <form onSubmit={handleSumbit}>
                            <List component="nav" aria-label="mailbox folders">
                                <ListItem>
                                    <div className="form-group" onChange={handleLicense}>
                                        <TextField
                                            id="outlined-helperText"
                                            label="Licencja"
                                            defaultValue={license}
                                            type="licencja"
                                            helperText="Wprowadź licencję"
                                        />
                                        <div className="form-group">
                                            {licenseError}
                                        </div>
                                    </div>
                                </ListItem>
                            </List>
                            <div className="form-group">
                                {dataError}
                            </div>
                        </form>
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleConfirm} disabled={!license}>Ok</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleConfirmClose}>
                <DialogTitle>Czy na pewno chcesz dodać poziom dostępu zarządcy?</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>Nie</Button>
                    <Button onClick={handleConfirmConfirm}>Tak</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={isOwner} onClose={handleClose}>
                <DialogTitle>Wypełnij formularz dodania uprawnieć właściciela</DialogTitle>
                <DialogContent>
                    <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                        <form onSubmit={handleSumbit}>
                            <List component="nav" aria-label="mailbox folders">
                                <ListItem>
                                    <div className="form-group" onChange={handlePhoneNumber}>
                                        <TextField
                                            id="outlined-helperText"
                                            label="Numer telefonu"
                                            defaultValue={phoneNumber}
                                            type="phoneNumber"
                                            helperText="Wprowadź numer telefonu"
                                        />
                                        <div className="form-group">
                                            {phoneNumberError}
                                        </div>
                                    </div>
                                </ListItem>
                            </List>
                            <div className="form-group">
                                {dataError}
                            </div>
                        </form>
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleConfirm} disabled={!phoneNumberValid}>Ok</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleConfirmClose}>
                <DialogTitle>Czy na pewno chcesz dodać poziom dostępu właściciela?</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>Nie</Button>
                    <Button onClick={handleConfirmConfirm}>Tak</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={isAdmin} onClose={handleClose}>
                <DialogTitle>Czy na pewno chcesz dodać poziom dostępu admin?</DialogTitle>
                <DialogActions>
                    <Button onClick={handleClose}>Nie</Button>
                    <Button onClick={handleConfirmConfirm}>Tak</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={successOpen}>
                <DialogTitle>Poziom dostępu został dodany</DialogTitle>
                <Button onClick={handleSuccessClose}>Ok</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={errorOpen}>
                <DialogTitle>{errorOpenMessage}</DialogTitle>
                <Button onClick={handleErrorClose}>Ok</Button>
            </Dialog>
        </div>
    );
}