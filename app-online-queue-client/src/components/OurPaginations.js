import React, {Component} from 'react';
import Pagination from "react-js-pagination"
require("bootstrap/dist/css/bootstrap.css");

class OurPaginations extends Component {
  render() {
    const {activePage, totalElements, size, showPageCount, changePage} = this.props;
    return (
      <div>
        <Pagination
          activePage={activePage}
          itemsCountPerPage={size}
          totalItemsCount={totalElements}
          pageRangeDisplayed={showPageCount}
          onChange={changePage.bind(this)}
        />
      </div>
    );
  }
}

OurPaginations.propTypes = {};

export default OurPaginations;
