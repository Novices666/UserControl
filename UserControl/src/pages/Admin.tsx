import {PageHeaderWrapper} from '@ant-design/pro-components';
import React from 'react';

const Admin: React.FC = (a)=> {
  return (
    <PageHeaderWrapper>
      {a.children}
    </PageHeaderWrapper>
  );
};
export default Admin;
