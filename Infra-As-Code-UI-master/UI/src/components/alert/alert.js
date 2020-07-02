import swal from "sweetalert";
export function confirmationBox(title, callback) {
  swal({
    title: title,
    buttons: true,
  }).then((value) => {
    callback && callback();
  });
}

export function alertBox(title, callback) {
  swal({
    title: title,
  }).then((value) => {
    callback && callback();
  });
}
