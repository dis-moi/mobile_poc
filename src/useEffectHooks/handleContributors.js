import React from 'react';
import SharedPreferences from 'react-native-shared-preferences';

function HandleContributorsEffect(
  radioButtonThatIsActivated,
  setItemIds,
  itemIds
) {
  const [contributors, setContributors] = React.useState([]);
  const [filteredContributors, setFilteredContributors] = React.useState([]);

  function sortNumberOfPublishedNoticeFromHighestToLowest(contributorsToSort) {
    return contributorsToSort.sort(function (previous, next) {
      return (
        next.contribution.numberOfPublishedNotices -
        previous.contribution.numberOfPublishedNotices
      );
    });
  }

  React.useEffect(() => {
    async function getContributors() {
      SharedPreferences.getAll(async function (values) {
        const ids = [...new Set(values.map((res) => res[1]))];

        const contributorsFromV3api = await fetch(
          'https://notices.bulles.fr/api/v3/contributors'
        ).then((response) => {
          return response.json();
        });
        const contributorsSorted = sortNumberOfPublishedNoticeFromHighestToLowest(
          contributorsFromV3api.filter(
            (contributor) => !ids.includes(String(contributor.id))
          )
        );
        const contributorsFollowedSorted = sortNumberOfPublishedNoticeFromHighestToLowest(
          contributorsFromV3api.filter((contributor) =>
            ids.includes(String(contributor.id))
          )
        );
        setContributors([...contributorsFollowedSorted, ...contributorsSorted]);
        setItemIds(ids);
      });
    }
    getContributors();
  }, [radioButtonThatIsActivated, setItemIds]);

  React.useEffect(() => {
    if (radioButtonThatIsActivated !== 'ALL') {
      const filteredContributorsByCategories = contributors.filter((res) =>
        res.categories.includes(radioButtonThatIsActivated)
      );

      const contributorsSorted = sortNumberOfPublishedNoticeFromHighestToLowest(
        filteredContributorsByCategories.filter(
          (contributor) => !itemIds.includes(String(contributor.id))
        )
      );
      const contributorsFollowedSorted = sortNumberOfPublishedNoticeFromHighestToLowest(
        filteredContributorsByCategories.filter((contributor) =>
          itemIds.includes(String(contributor.id))
        )
      );

      setFilteredContributors([
        ...contributorsFollowedSorted,
        ...contributorsSorted,
      ]);
    }
  }, [radioButtonThatIsActivated, contributors, itemIds]);

  return { filteredContributors, contributors };
}

export default HandleContributorsEffect;
