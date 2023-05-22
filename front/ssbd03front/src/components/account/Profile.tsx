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
import {ADMIN, API_URL, MANAGER, OWNER} from "../../consts";
import {useCookies} from "react-cookie";
import axios from 'axios';
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import {useTranslation} from "react-i18next";
import {Account} from "../../types/account";
import EditUserPersonalData from "../personalData/EditUserPersonalData";
import EnableAccount from "../accounts/EnableAccount";
import DisableAccount from "../accounts/DisableAccount";
import EditUserPassword from "../passwords/EditUserPassword";
import EditUserEmail from "../email/EditUserEmail";
import UserIcon from "../icons/UserIcon";

const roles = [
    {value: ADMIN, label: "Administrator"},
    {value: MANAGER, label: "Zarządca"},
    {value: OWNER, label: "Właściciel"}
];

export default function Profile() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [cookies, setCookie, removeCookie] = useCookies(["token", "role"]);
    const token = "Bearer " + cookies.token;
    const [etag, setEtag] = useState(false);
    const [version, setVersion] = useState("");
    const [selectedRole, setSelectedRole] = useState("");
    const [license, setLicense] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [role, setRole] = useState(cookies.role);
    const username = useParams().username;
    const [account, setAccount] = useState<Account | null>(null);
    const [phoneNumberError, setPhoneNumberError] = useState("");
    const [licenseError, setLicenseError] = useState("");
    const [phoneNumberValid, setPhoneNumberValid] = useState(false);
    const [removeValid, setRemoveValid] = useState(false);
    const [licenseValid, setLicenseValid] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [successOpen, setSuccessOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = useState("");
    const [dataError, setDataError] = useState("");
    const [isAdmin, setIsAdmin] = useState(false);
    const [isOwner, setIsOwner] = useState(false);
    const [isManager, setIsManager] = useState(false);
    const [isRemoveAccessOpen, setIsRemoveAccessOpen] = useState(false);
    const [confirmRemove, setConfirmRemove] = useState(false);
    const [successOpenRemove, setSuccessOpenRemove] = useState(false);

    const fetchData = async () => {
        axios.get(`${API_URL}/accounts/${username}`, {
            headers: {
                'Authorization': token
            }
        }).then(response => {
            setAccount(response.data);
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
                license: license.toString(),
                version: parseInt(version)
            }

            axios.patch(`${API_URL}/accounts/add-access-level-manager`,
                addAccessLevelManagerDTO, {
                    headers: {
                        'Authorization': token,
                        'If-Match': etag,
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
                phoneNumber: phoneNumber.toString(),
                version: parseInt(version)
            }

            axios.patch(`${API_URL}/accounts/add-access-level-owner`,
                addAccessLevelOwnerDTO, {
                    headers: {
                        'Authorization': token,
                        'If-Match': etag,
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
                username: username,
                version: parseInt(version)
            }

            axios.patch(`${API_URL}/accounts/add-access-level-admin`,
                addAccessLevelAdminDTO, {
                    headers: {
                        'Authorization': token,
                        'If-Match': etag,
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
                accessLevel: selectedRole.toString(),
                version: parseInt(version)
            }

            axios.patch(`${API_URL}/accounts/revoke-access-level`,
                removeAccessLevelDTO, {
                    headers: {
                        'Authorization': token,
                        'If-Match': etag,
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
        window.location.reload();
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
        const fetchData = async () => {
            await axios.get(`${API_URL}/accounts/${username}`, {
                headers: {
                    Authorization: token
                }
            })
                .then(response => {
                    setEtag(response.headers["etag"]);
                    setVersion(response.data.version)
                });
        };
        fetchData();
        setIsManager(true);
    };

    const handleClickOpenOwner = () => {
        const fetchData = async () => {
            await axios.get(`${API_URL}/accounts/${username}`, {
                headers: {
                    Authorization: token
                }
            })
                .then(response => {
                    setEtag(response.headers["etag"]);
                    setVersion(response.data.version)
                });
        };
        fetchData();
        setIsOwner(true);
    };

    const handleClickOpenAdmin = () => {
        const fetchData = async () => {
            await axios.get(`${API_URL}/accounts/${username}`, {
                headers: {
                    Authorization: token
                }
            })
                .then(response => {
                    setEtag(response.headers["etag"]);
                    setVersion(response.data.version)
                });
        };
        fetchData();
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
        const fetchData = async () => {
            await axios.get(`${API_URL}/accounts/${username}`, {
                headers: {
                    Authorization: token
                }
            })
                .then(response => {
                    setEtag(response.headers["etag"]);
                    setVersion(response.data.version)
                });
        };
        fetchData();
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
                                <Paper elevation={3} style={{position: 'relative'}}>
                                    <div style={{position: 'absolute', top: '1vh', right: '1vh'}}>
                                        <EditUserPersonalData/>
                                    </div>
                                    <Typography sx={{padding: '1vh'}} variant="h5">
                                        <b>{t('personal_data.name')}:</b> {account.firstName}
                                    </Typography>
                                    <Typography sx={{padding: '1vh'}} variant="h5">
                                        <b>{t('personal_data.surname')}:</b> {account.surname}
                                    </Typography>
                                </Paper>
                                <Paper elevation={3} style={{position: 'relative'}}>
                                    <Typography sx={{padding: '1vh'}}
                                                variant="h5"><b>{t('login.username')}:</b> {account.username}
                                    </Typography>
                                </Paper>
                                <Paper elevation={3} style={{position: 'relative'}}>
                                    <div style={{position: 'absolute', top: '1vh', right: '1vh'}}>
                                        <EditUserEmail/>
                                    </div>
                                    <Typography sx={{padding: '1vh'}}
                                                variant="h5"><b>{t('register.email')}:</b> {account.email}</Typography>
                                </Paper>

                            {(account.phoneNumber && account.isUserOwner) && (
                                <Paper elevation={3} style={{position: 'relative'}}>
                                    <Typography sx={{padding: '1vh'}}
                                                variant="h5"><b>{t('register.phone_number')}:</b> {account.phoneNumber}
                                    </Typography>
                                </Paper>
                            )}
                            <Paper elevation={3} style={{position: 'relative'}}>
                                <div style={{
                                    position: 'absolute',
                                    top: '1vh',
                                    right: '1vh',
                                    display: 'flex',
                                    gap: '0.5vh'
                                }}>
                                    {(!account.isEnable && ((!account.isUserAdmin && role === MANAGER) || role === ADMIN)) && <EnableAccount/>}
                                    {account.isEnable && ((!account.isUserAdmin && role === MANAGER) || role === ADMIN) && <DisableAccount/>}
                                </div>
                                <Typography sx={{padding: '1vh'}}
                                            variant="h5"><b>{t('enable_account.enable')}:</b> {account.isEnable ? t('enable_account.enable') : t('disable_account.disable')}
                                </Typography>
                            </Paper>
                            <Paper elevation={3} style={{position: 'relative'}}>
                                <Typography sx={{padding: '1vh'}}
                                            variant="h5"><b>{t('account_list.active_status')}:</b> {account.isActive ? t('account_list.active') : t('account_list.inactive')}
                                </Typography>
                            </Paper>
                            {!account.isUserManager && (
                                <Paper elevation={3} style={{position: 'relative'}}>
                                    <div style={{
                                        position: 'absolute',
                                        top: '1vh',
                                        right: '1vh',
                                        display: 'flex',
                                        gap: '0.5vh'
                                    }}>
                                        <EditUserPassword/>
                                    </div>
                                    <Typography sx={{padding: '1vh'}}
                                                variant="h5"><b>{t('profile.user_password')}{account.username}</b>
                                    </Typography>
                                </Paper>
                            )}
                            {(account.license && account.isUserManager) && (
                                <Paper elevation={3} style={{position: 'relative'}}>
                                    <Typography sx={{padding: '1vh'}}
                                                variant="h5"><b>{t('profile.license')}:</b> {account.license}
                                    </Typography>
                                </Paper>
                            )}
                            <div style={{display: 'flex', alignItems: 'center'}}>
                                <Typography sx={{padding: '1vh'}} variant="h5">
                                    <b>{t('profile.access_levels')}:</b>
                                </Typography>
                                <FormControlLabel
                                    control={<Checkbox checked={account.isUserOwner} disabled/>}
                                    label={t('profile.owner')}
                                />
                                <FormControlLabel
                                    control={<Checkbox checked={account.isUserManager} disabled/>}
                                    label={t('profile.manager')}
                                />
                                <FormControlLabel
                                    control={<Checkbox checked={account.isUserAdmin} disabled/>}
                                    label={t('profile.admin')}
                                />
                            </div>
                            <div style={{display: "flex", justifyContent: "center", alignItems: "center"}}>
                                {role.includes(ADMIN) && (
                                    <>
                                        {!account.isUserOwner && (
                                            <Button onClick={handleClickOpenOwner} variant="contained" style={{height: "80px", margin: "10px"}}>
                                                {t('profile.add')}<br/>{t('profile.access_level')}<br/>{t('profile.owner')}
                                            </Button>
                                        )}
                                        {!account.isUserManager && (
                                            <Button onClick={handleClickOpenManager} variant="contained"
                                                    style={{height: "80px", margin: "10px"}}>
                                                {t('profile.add')}<br/>{t('profile.access_level')}<br/>{t('profile.manager')}
                                            </Button>
                                        )}
                                        {!account.isUserAdmin && (
                                            <Button onClick={handleClickOpenAdmin} variant="contained"
                                                    style={{height: "80px", margin: "10px"}}>
                                                {t('profile.add')}<br/>{t('profile.access_level')}<br/>{t('profile.admin')}
                                            </Button>
                                        )}
                                        <Button onClick={handleRemoveAccessLevel} variant="contained"
                                                style={{height: "80px", margin: "10px"}}>
                                            {t('profile.del')}<br/>{t('profile.access_level')}
                                        </Button>
                                    </>
                                )}
                            <Dialog disableEscapeKeyDown open={isManager} onClose={handleClose}>
                                <DialogTitle>{t('profile.access_level_form_title')}</DialogTitle>
                                <DialogContent>
                                    <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                                        <form onSubmit={handleSumbit}>
                                            <List component="nav" aria-label="mailbox folders">
                                                <ListItem>
                                                    <div className="form-group" onChange={handleLicense}>
                                                        <TextField
                                                            id="outlined-helperText"
                                                            label={t('profile.license')}
                                                            defaultValue={license}
                                                            type="licencja"
                                                            helperText={t('profile.set_license')}
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
                                    <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
                                    <Button onClick={handleConfirm} disabled={!licenseValid}>{t('profile.add')}</Button>
                                </DialogActions>
                            </Dialog>

                                    <Dialog disableEscapeKeyDown open={isManager && confirmOpen}
                                            onClose={handleConfirmClose}>
                                        <DialogTitle>{t('profile.add_level')}</DialogTitle>
                                        <DialogActions>
                                            <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                                            <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                                        </DialogActions>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={isOwner} onClose={handleClose}>
                                        <DialogTitle>{t('profile.owner_form')}</DialogTitle>
                                        <DialogContent>
                                            <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                                                <form onSubmit={handleSumbit}>
                                                    <List component="nav" aria-label="mailbox folders">
                                                        <ListItem>
                                                            <div className="form-group" onChange={handlePhoneNumber}>
                                                                <TextField
                                                                    id="outlined-helperText"
                                                                    label={t('register.phone_number')}
                                                                    defaultValue={phoneNumber}
                                                                    type="phoneNumber"
                                                                    helperText={t('profile.set_phone_number')}
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
                                            <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
                                            <Button onClick={handleConfirm}
                                                    disabled={!phoneNumberValid}>{t('profile.add')}</Button>
                                        </DialogActions>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={isOwner && confirmOpen}
                                            onClose={handleConfirmClose}>
                                        <DialogTitle>{t('profile.add_level')}</DialogTitle>
                                        <DialogActions>
                                            <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                                            <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                                        </DialogActions>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={isAdmin} onClose={handleClose}>
                                        <DialogTitle>{t('profile.add_level')}</DialogTitle>
                                        <DialogActions>
                                            <Button onClick={handleClose}>{t('confirm.no')}</Button>
                                            <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                                        </DialogActions>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={isRemoveAccessOpen} onClose={handleClose}>
                                        <DialogTitle>{t('profile.revoke_level')}</DialogTitle>
                                        <DialogContent>
                                            <form onSubmit={handleAddSubmit}>
                                                <FormControl fullWidth>
                                                    {!selectedRole &&
                                                        <InputLabel
                                                            id="access-level-label">{t('profile.select_access_level')}</InputLabel>}
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
                                            <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
                                            <Button onClick={handleConfirmRemove}
                                                    disabled={!removeValid}>{t('profile.del')}</Button>
                                        </DialogActions>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={confirmRemove} onClose={handleConfirmClose}>
                                        <DialogTitle>{t('profile.revoke_level')}</DialogTitle>
                                        <DialogActions>
                                            <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                                            <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                                        </DialogActions>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={successOpenRemove}>
                                        <DialogTitle>{t('profile.revoked_level')}</DialogTitle>
                                        <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={successOpen}>
                                        <DialogTitle>{t('profile.added_level')}</DialogTitle>
                                        <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={errorOpen}>
                                        <DialogTitle>{errorOpenMessage}</DialogTitle>
                                        <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
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