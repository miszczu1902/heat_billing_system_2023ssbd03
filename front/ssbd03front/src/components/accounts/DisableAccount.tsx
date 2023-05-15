import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogTitle from '@mui/material/DialogTitle';
import axios from 'axios'; 
import { API_URL } from '../../consts';
import { useParams } from "react-router-dom";
import { useCookies } from 'react-cookie';
import { useEffect } from 'react';

export default function DisableAccount() {
    const username = useParams().username;
    const [cookies, setCookie] = useCookies(["token", "etag"]);
    const token = "Bearer " + cookies.token;
    const etag = cookies.etag;
  
    const [open, setOpen] = React.useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);
  
    const [successOpen, setSuccessOpen] = React.useState(false);
    const [errorOpen, setErrorOpen] = React.useState(false);

    useEffect(() => {
      const fetchData = async () => {
        await axios.get(`${API_URL}/accounts/${username}`, {
          headers: {
            Authorization: token
          }
        })
        .then(response => {
          setCookie("etag", response.headers.etag);
        });
    };
    fetchData();
    }, []);

  
    const handleClickOpen = () => {
      setOpen(true);
    };
  
    const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
      if (reason !== 'backdropClick') {
        setOpen(false);
      }
    }
  
    const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
      if (reason !== 'backdropClick') {
        setConfirmOpen(false);
      }
      const fetchData = async () => {
        axios.patch(`${API_URL}/accounts/${username}/disable`, {}, {
           headers: {
            'Authorization': token,
            'If-Match': etag
          },
        })
        .then(response => {
          setSuccessOpen(true);
        })
        .catch(error => {
          setErrorOpen(true);
        });
      };
        fetchData();
      handleConfirmClose(event, reason);
    }
  
    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
      if (reason !== 'backdropClick') {
        setSuccessOpen(false);
      }
    }
  
    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
      if (reason !== 'backdropClick') {
        setErrorOpen(false);
      }
    };
  
    return (
      <div>
        <div>
        <Button onClick={handleClickOpen} variant="contained">Zablokuj</Button>
        </div>
        <Dialog disableEscapeKeyDown open={open}>
          <DialogTitle>Czy na pewno chcesz zablokować użytkownika {username} ?</DialogTitle>
          <DialogActions>
            <Button onClick={handleConfirmClose}>Nie</Button>
            <Button onClick={handleConfirmConfirm}>Tak</Button>
          </DialogActions>
        </Dialog>
  
        <Dialog disableEscapeKeyDown open={successOpen}>
          <DialogTitle>Użytkownik {username} został zablokowany</DialogTitle>
          <Button onClick={handleSuccessClose}>Ok</Button>
        </Dialog>
  
        <Dialog disableEscapeKeyDown open={errorOpen}>
          <DialogTitle>Wystąpił błąd podczas zablokowania użytkownika {username}</DialogTitle>
          <Button onClick={handleErrorClose}>Ok</Button>
        </Dialog>
      </div>
    );
}