import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogTitle from '@mui/material/DialogTitle';
import axios from 'axios'; 
import { API_URL } from '../../consts';
import { useParams} from "react-router-dom";


export default function EnableAccount() {
  const username = useParams().username;
  const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjgzOTE1MzUwLCJyb2xlIjoiQURNSU4iLCJleHAiOjE2ODM5MTcxNTB9.tmNxVtVg18vwfhRZNEQFY1h8CFR2fF-oVSlR0CXKyoU';

  const [open, setOpen] = React.useState(false);
  const [confirmOpen, setConfirmOpen] = React.useState(false);

  const [successOpen, setSuccessOpen] = React.useState(false);
  const [errorOpen, setErrorOpen] = React.useState(false);

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
      axios.patch(`${API_URL}/accounts/${username}/enable`, {},
      {
         headers: {
          'Authorization': 'Bearer ' + token,
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
      <Button onClick={handleClickOpen} variant="contained">Odblokuj</Button>
      </div>
      <Dialog disableEscapeKeyDown open={open}>
        <DialogTitle>Czy na pewno chcesz odblokować użytkownika {username} ?</DialogTitle>
        <DialogActions>
          <Button onClick={handleConfirmClose}>Nie</Button>
          <Button onClick={handleConfirmConfirm}>Tak</Button>
        </DialogActions>
      </Dialog>

      <Dialog disableEscapeKeyDown open={successOpen}>
        <DialogTitle>Użytkownik {username} został odblokowany</DialogTitle>
        <Button onClick={handleSuccessClose}>Ok</Button>
      </Dialog>

      <Dialog disableEscapeKeyDown open={errorOpen}>
        <DialogTitle>Wystąpił błąd podczas odblokowania użytkownika {username}</DialogTitle>
        <Button onClick={handleErrorClose}>Ok</Button>
      </Dialog>
    </div>
  );
}