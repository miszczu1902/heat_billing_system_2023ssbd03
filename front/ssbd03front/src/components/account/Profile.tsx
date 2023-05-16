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

    const [confirmOpenAdmin, setConfirmOpenAdmin] = React.useState(false);
    const [successOpenAdmin, setSuccessOpenAdmin] = React.useState(false);
    const [errorOpenAdmin, setErrorOpenAdmin] = React.useState(false);
    const [errorOpenMessageAdmin, setErrorOpenMessageAdmin] = React.useState("");
    const [openAdmin, setOpenAdmin] = React.useState(false);

    const [confirmOpenOwner, setConfirmOpenOwner] = React.useState(false);
    const [successOpenOwner, setSuccessOpenOwner] = React.useState(false);
    const [errorOpenOwner, setErrorOpenOwner] = React.useState(false);
    const [errorOpenMessageOwner, setErrorOpenMessageOwner] = React.useState("");
    const [openOwner, setOpenOwner] = React.useState(false);
    const [phoneNumberError, setPhoneNumberError] = React.useState("");
    const [phoneNumberValid, setPhoneNumberValid] = React.useState(false);

    const [confirmOpenManager, setConfirmOpenManager] = React.useState(false);
    const [successOpenManager, setSuccessOpenManager] = React.useState(false);
    const [errorOpenManager, setErrorOpenManager] = React.useState(false);
    const [errorOpenMessageManager, setErrorOpenMessageManager] = React.useState("");
    const [openManager, setOpenManager] = React.useState(false);
    const [licenseError, setLicenseError] = React.useState("");
    const [licenseValid, setLicenseValid] = React.useState(false);

    const [dataError, setDataError] = React.useState("");



    ///////////////////////

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

    const handleConfirmConfirmManager = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpenManager(false);
        }
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
                setSuccessOpenManager(true);
            })
            .catch(error => {
                setErrorOpenMessageManager(error.response.data.message)
                setErrorOpenManager(true);
            });
        handleCloseManager(event, reason);
    }

    const handleCloseManager = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setOpenManager(false);
        }
    };

    const handleErrorCloseManager = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpenManager(false);
            setConfirmOpenManager(false);
        }
    };

    const handleClickOpenManager = () => {
        setOpenManager(true);
    };

    const handleSuccessCloseManager = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        setLicense("");
        if (reason !== 'backdropClick') {
            setSuccessOpenManager(false);
        }
    }

    const handleConfirmManager = () => {
        if (licenseValid) {
            setDataError("");
            setConfirmOpenManager(true);
        } else {
            setDataError("Wprowadź poprawne dane");
        }
    }

    const handleSumbitManager = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const handleConfirmCloseManager = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpenManager(false);
        }
    }

//////////////////
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

    const handleConfirmConfirmOwner = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpenAdmin(false);
        }
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
                setSuccessOpenAdmin(true);
            })
            .catch(error => {
                setErrorOpenMessageAdmin(error.response.data.message)
                setErrorOpenAdmin(true);
            });
        handleCloseOwner(event, reason);
    }

    const handleCloseOwner = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setOpenOwner(false);
        }
    };

    const handleErrorCloseOwner = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpenOwner(false);
            setConfirmOpenOwner(false);
        }
    };

    const handleClickOpenOwner = () => {
        setOpenOwner(true);
    };

    const handleSuccessCloseOwner = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        setPhoneNumber("");
        if (reason !== 'backdropClick') {
            setSuccessOpenOwner(false);
        }
    }

    const handleConfirmOwner = () => {
        if (phoneNumberValid) {
            setDataError("");
            setConfirmOpenOwner(true);
        } else {
            setDataError("Wprowadź poprawne dane");
        }
    }

    const handleSumbitOwner = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const handleConfirmCloseOwner = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpenOwner(false);
        }
    }

    /////////////

    const handleConfirmConfirmAdmin = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpenAdmin(false);
        }
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
                setSuccessOpenAdmin(true);
            })
            .catch(error => {
                setErrorOpenMessageAdmin(error.response.data.message)
                setErrorOpenAdmin(true);
            });
        handleCloseAdmin(event, reason);
    }

    const handleClickOpenAdmin = () => {
        setOpenAdmin(true);
    };

    const handleCloseAdmin = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setOpenAdmin(false);
        }
    };
    const handleErrorCloseAdmin = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpenAdmin(false);
        }
    };

    const handleSuccessCloseAdmin = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        setPhoneNumber("");
        if (reason !== 'backdropClick') {
            setSuccessOpenAdmin(false);
        }
    }
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

            <Dialog disableEscapeKeyDown open={openManager} onClose={handleCloseManager}>
                <DialogTitle>Wypełnij formularz dodania uprawnien zarządcy</DialogTitle>
                <DialogContent>
                    <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                        <form onSubmit={handleSumbitManager}>
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
                    <Button onClick={handleCloseManager}>Cancel</Button>
                    <Button onClick={handleConfirmManager} disabled={!license}>Ok</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={confirmOpenManager} onClose={handleConfirmCloseManager}>
                <DialogTitle>Czy na pewno chcesz dodać poziom dostępu zarządcy?</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmCloseManager}>Nie</Button>
                    <Button onClick={handleConfirmConfirmManager}>Tak</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={successOpenManager}>
                <DialogTitle>Poziom dostępu zarzadcy został dodany</DialogTitle>
                <Button onClick={handleSuccessCloseManager}>Ok</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={errorOpenManager}>
                <DialogTitle>{errorOpenMessageManager}</DialogTitle>
                <Button onClick={handleErrorCloseManager}>Ok</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={openOwner} onClose={handleCloseAdmin}>
                <DialogTitle>Wypełnij formularz dodania uprawnieć właściciela</DialogTitle>
                <DialogContent>
                    <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                        <form onSubmit={handleSumbitOwner}>
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
                    <Button onClick={handleCloseOwner}>Cancel</Button>
                    <Button onClick={handleConfirmOwner} disabled={!phoneNumberValid}>Ok</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={confirmOpenOwner} onClose={handleConfirmCloseOwner}>
                <DialogTitle>Czy na pewno chcesz dodać poziom dostępu właściciela?</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmCloseOwner}>Nie</Button>
                    <Button onClick={handleConfirmConfirmOwner}>Tak</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={successOpenOwner}>
                <DialogTitle>Poziom dostępu właściciela został dodany</DialogTitle>
                <Button onClick={handleSuccessCloseOwner}>Ok</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={errorOpenOwner}>
                <DialogTitle>{errorOpenMessageOwner}</DialogTitle>
                <Button onClick={handleErrorCloseOwner}>Ok</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={openAdmin} onClose={handleCloseAdmin}>
                <DialogTitle>Czy na pewno chcesz dodać poziom dostępu admin?</DialogTitle>
                <DialogActions>
                    <Button onClick={handleCloseAdmin}>Nie</Button>
                    <Button onClick={handleConfirmConfirmAdmin}>Tak</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={successOpenAdmin}>
                <DialogTitle>Poziom dostępu został dodany</DialogTitle>
                <Button onClick={handleSuccessCloseAdmin}>Ok</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={errorOpenAdmin}>
                <DialogTitle>{errorOpenMessageAdmin}</DialogTitle>
                <Button onClick={handleErrorCloseAdmin}>Ok</Button>
            </Dialog>
        </div>
    );

}