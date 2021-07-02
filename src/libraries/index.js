import moment from 'moment';

export function isValidHttpUrl(str) {
  const pattern = new RegExp(
    '^(https?:\\/\\/)?' + // protocol
      '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
      '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
      '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
      '(\\?[;&a-z\\d%_.~+=-]*)?' + // query string
      '(\\#[-a-z\\d_]*)?$',
    'i'
  ); // fragment locator
  return !!pattern.test(str);
}

export function formatDate(notFormattedDate) {
  const capitalize = (s) => {
    if (typeof s !== 'string') {
      return '';
    }
    return s.charAt(0).toUpperCase() + s.slice(1);
  };

  var month = moment(notFormattedDate.modified, 'YYYY-MM-DD')
    .locale('fr')
    .format('MMMM');
  var day = moment(notFormattedDate.modified, 'YYYY-MM-DD').format('D');

  var year = moment(notFormattedDate.modified, 'YYYY-MM-DD').format('Y');

  const formattedDate = `${moment(notFormattedDate.modified, 'YYYY-MM-DD')
    .locale('fr')
    .format('dddd')} ${day} ${month} ${year}`;

  return capitalize(formattedDate);
}
