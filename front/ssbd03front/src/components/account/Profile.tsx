import Box from "@mui/material/Box";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Select, {SelectChangeEvent} from '@mui/material/Select';
import React, {useEffect, useState} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import TextField from '@mui/material/TextField';
import {Checkbox, FormControlLabel, Grid} from '@mui/material';
import {useNavigate, useParams} from "react-router-dom";
import {API_URL} from "../../consts";
import {useCookies} from "react-cookie";
import axios from 'axios';
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import {useTranslation} from "react-i18next";
import {Account} from "../../types/account";

const roles = [
    {value: "ADMIN", label: "Administrator"},
    {value: "MANAGER", label: "Manager"},
    {value: "OWNER", label: "Właściciel"}
];

export default function Profile() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [cookies] = useCookies(["token"]);
    const token = "Bearer " + cookies.token;
    const [selectedRole, setSelectedRole] = useState("");
    const [license, setLicense] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const username = useParams().username;
    const [account, setAccount] = useState<Account | null>(null);
    const [phoneNumberError, setPhoneNumberError] = React.useState("");
    const [licenseError, setLicenseError] = React.useState("");
    const [phoneNumberValid, setPhoneNumberValid] = React.useState(false);
    const [removeValid, setRemoveValid] = React.useState(false);
    const [licenseValid, setLicenseValid] = React.useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);
    const [successOpen, setSuccessOpen] = React.useState(false);
    const [errorOpen, setErrorOpen] = React.useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = React.useState("");
    const [dataError, setDataError] = React.useState("");
    const [isAdmin, setIsAdmin] = React.useState(false);
    const [isOwner, setIsOwner] = React.useState(false);
    const [isManager, setIsManager] = React.useState(false);
    const [isUserAdmin, setIsUserAdmin] = useState(false);
    const [isUserManager, setIsUserManager] = useState(false);
    const [isUserOwner, setIsUserOwner] = useState(false);
    const [isRemoveAccessOpen, setIsRemoveAccessOpen] = useState(false);
    const [confirmRemove, setConfirmRemove] = React.useState(false);
    const [successOpenRemove, setSuccessOpenRemove] = React.useState(false);

    const UserIcon = ({width = 24, height = 24}) => (
        <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" fill="currentColor" className="bi bi-person-fill"
             viewBox="0 0 16 16">
            <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3Zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z"/>
        </svg>
    )

    const fetchData = async () => {
        axios.get(`${API_URL}/accounts/${username}`, {
            headers: {
                'Authorization': token
            }
        }).then(response => {
            setAccount(response.data);

            const accessLevels = response.data.accessLevels;
            setIsUserOwner(accessLevels.includes('OWNER'));
            setIsUserManager(accessLevels.includes('MANAGER'));
            setIsUserAdmin(accessLevels.includes('ADMIN'));
        }).catch(error => {
            if (error.response.status == 403) navigate('/');
        });
    };

    useEffect(() => {
        fetchData();
    }, [username]);

    const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
            setConfirmRemove(false);
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
        if (confirmRemove && !isAdmin && !isOwner && !isManager) {
            const removeAccessLevelDTO = {
                username: username,
                accessLevel: selectedRole.toString()
            }

            axios.patch(`${API_URL}/accounts/revoke-access-level`,
                removeAccessLevelDTO, {
                    headers: {
                        'Authorization': token,
                        'Content-Type': 'application/json'
                    },
                })
                .then(response => {
                    setSuccessOpenRemove(true);
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
            setIsRemoveAccessOpen(false);
        }
    };

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpen(false);
            setConfirmOpen(false);
            setIsManager(false);
            setIsOwner(false);
            setIsAdmin(false);
            setIsRemoveAccessOpen(false);
        }
    };

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        setLicense("");
        setPhoneNumber("");
        setIsManager(false);
        setIsOwner(false);
        setIsAdmin(false);
        setIsRemoveAccessOpen(false);
        setConfirmRemove(false);
        setSuccessOpenRemove(false);
        if (reason !== 'backdropClick') {
            setSuccessOpen(false);
        }
    }

    const handleConfirm = () => {
        if (licenseValid || phoneNumberValid) {
            setDataError("");
            setConfirmOpen(true);
        } else {
            setDataError("");
        }
    }

    const handleConfirmRemove = () => {
        if (isRemoveAccessOpen) {
            setDataError("");
            setConfirmRemove(true);
        } else {
            setDataError("");
        }
    }

    const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
            setConfirmRemove(false);
        }
    }

    const handlePhoneNumber = (event: React.ChangeEvent<HTMLInputElement>) => {
        let phoneNumber = event.target.value;
        setPhoneNumber(phoneNumber);
        const regex = /^\d{9}$/;
        if (!regex.test(phoneNumber)) {
            setPhoneNumberError(t('profile.phone_number_error'));
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
            setLicenseError(t('profile.license_error'));
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

    const handleAddSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
    };

    const handleAccessLevelChange = (event: SelectChangeEvent<string>) => {
        const selectedRole = event.target.value;
        setSelectedRole(selectedRole);
        setRemoveValid(true);
    };

    const handleRemoveAccessLevel = () => {
        setIsRemoveAccessOpen(true);
    };

    return (
        <div style={{height: '93.3vh', width: '100vw', boxSizing: 'border-box', left: 0, right: 0, bottom: 0}}>
            <Grid container justifyContent="center" alignItems="center"
                  sx={{background: '#1c8de4', height: '100%', width: '100%'}}>
                <Grid my={2} item sm={8} md={5} component={Paper} elevation={6}>
                    <Box component="form" sx={{
                        display: 'flex',
                        flexWrap: 'wrap',
                        alignItems: 'center',
                        justifyContent: 'center',
                        margin: '2vh'
                    }}>
                        <Typography sx={{padding: '1vh'}} variant="h4">{t('profile.title')}</Typography>
                        <UserIcon/>
                    </Box>
                    <Box sx={{my: 30, display: 'flex', flexDirection: 'column', alignItems: 'left', margin: '2vh'}}>
                        {account !== null && (
                            <>
                                <Typography sx={{padding: '1vh'}} variant="h5"><b>{t('personal_data.name')}:</b> {account.firstName}
                                </Typography>
                                <Typography sx={{padding: '1vh'}} variant="h5"><b>{t('personal_data.surname')}:</b> {account.surname}
                                </Typography>
                                <Typography sx={{padding: '1vh'}} variant="h5"><b>{t('login.username')}:</b> {account.username}
                                </Typography>
                                <Typography sx={{padding: '1vh'}} variant="h5"><b>{t('register.email')}:</b> {account.email}
                                </Typography>
                                {account.phoneNumber && (
                                    <Typography sx={{padding: '1vh'}} variant="h5"><b>{t('register.phone_number')}:</b> {account.phoneNumber}</Typography>
                                )}
                                <Typography sx={{padding: '1vh'}}
                                            variant="h5"><b>{t('enable_account.enable')}:</b> {account.isEnable ? t('account_list.confirmed') : t('account_list.unconfirmed')}
                                </Typography>
                                <Typography sx={{padding: '1vh'}}
                                            variant="h5"><b>{t('account_list.active_status')}:</b> {account.isActive ? t('account_list.active') : t('account_list.inactive')}
                                </Typography>
                                {account.license && (
                                    <Typography sx={{padding: '1vh'}} variant="h5"><b>{t('profile.license')}:</b> {account.license}</Typography>
                                )}
                                <div style={{display: 'flex', alignItems: 'center'}}>
                                    <Typography sx={{padding: '1vh'}} variant="h5">
                                        <b>{t('profile.access_levels')}:</b>
                                    </Typography>
                                    <FormControlLabel
                                        control={<Checkbox checked={isUserOwner} disabled/>}
                                        label="Owner"
                                    />
                                    <FormControlLabel
                                        control={<Checkbox checked={isUserManager} disabled/>}
                                        label="Manager"
                                    />
                                    <FormControlLabel
                                        control={<Checkbox checked={isUserAdmin} disabled/>}
                                        label="Admin"
                                    />
                                </div>
                                <div style={{display: "flex", justifyContent: "center", alignItems: "center"}}>
                                    <Button onClick={handleClickOpenManager} variant="contained"
                                            style={{height: "80px", margin: "10px"}}>
                                        Dodaj<br/>poziom dostępu<br/>manager
                                    </Button>
                                    <Button onClick={handleClickOpenOwner} variant="contained"
                                            style={{height: "80px", margin: "10px"}}>
                                        Dodaj<br/>poziom dostępu<br/>owner
                                    </Button>
                                    <Button onClick={handleClickOpenAdmin} variant="contained"
                                            style={{height: "80px", margin: "10px"}}>
                                        Dodaj<br/>poziom dostępu<br/>admin
                                    </Button>
                                    <Button onClick={handleRemoveAccessLevel} variant="contained"
                                            style={{height: "80px", margin: "10px"}}>
                                        Usuń<br/>poziom dostępu
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
                                            <Button onClick={handleClose}>Anuluj</Button>
                                            <Button onClick={handleConfirm} disabled={!licenseValid}>Dodaj</Button>
                                        </DialogActions>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={isManager && confirmOpen}
                                            onClose={handleConfirmClose}>
                                        <DialogTitle>Czy na pewno chcesz dodać poziom dostępu zarządcy?</DialogTitle>
                                        <DialogActions>
                                            <Button onClick={handleConfirmClose}>Nie</Button>
                                            <Button onClick={handleConfirmConfirm}>Tak</Button>
                                        </DialogActions>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={isOwner} onClose={handleClose}>
                                        <DialogTitle>Wypełnij formularz dodania uprawnień właściciela</DialogTitle>
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
                                            <Button onClick={handleClose}>Anuluj</Button>
                                            <Button onClick={handleConfirm} disabled={!phoneNumberValid}>Dodaj</Button>
                                        </DialogActions>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={isOwner && confirmOpen}
                                            onClose={handleConfirmClose}>
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

                                    <Dialog disableEscapeKeyDown open={isRemoveAccessOpen} onClose={handleClose}>
                                        <DialogTitle>Usuń poziom dostępu</DialogTitle>
                                        <DialogContent>
                                            <form onSubmit={handleAddSubmit}>
                                                <FormControl fullWidth>
                                                    {!selectedRole &&
                                                        <InputLabel id="access-level-label">Wybierz poziom</InputLabel>}
                                                    <Select
                                                        labelId="access-level-label"
                                                        value={selectedRole}
                                                        onChange={(event: SelectChangeEvent<string>) => handleAccessLevelChange(event)}
                                                        displayEmpty
                                                    >
                                                        {roles.map((role) => (
                                                            <MenuItem key={role.value} value={role.value}>
                                                                {role.label}
                                                            </MenuItem>
                                                        ))}
                                                    </Select>
                                                </FormControl>
                                            </form>
                                        </DialogContent>
                                        <DialogActions>
                                            <Button onClick={handleClose}>Anuluj</Button>
                                            <Button onClick={handleConfirmRemove} disabled={!removeValid}>Usuń</Button>
                                        </DialogActions>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={confirmRemove} onClose={handleConfirmClose}>
                                        <DialogTitle>Czy na pewno chcesz odebrać poziom dostępu?</DialogTitle>
                                        <DialogActions>
                                            <Button onClick={handleConfirmClose}>Nie</Button>
                                            <Button onClick={handleConfirmConfirm}>Tak</Button>
                                        </DialogActions>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={successOpenRemove}>
                                        <DialogTitle>Poziom dostępu został odebrany</DialogTitle>
                                        <Button onClick={handleSuccessClose}>Ok</Button>
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
                            </>
                        )}
                    </Box>
                </Grid>
            </Grid>
        </div>
    );
}