import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {ProTable, TableDropdown} from '@ant-design/pro-components';
import {useRef} from 'react';
import {searchAll} from "@/services/ant-design-pro/api";


export const waitTimePromise = async (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

export const waitTime = async (time: number = 100) => {
  await waitTimePromise(time);
};


const columns: ProColumns<API.CurrentUser>[] = [
  {
    title:'id',
    dataIndex: 'id',
    valueType: 'indexBorder',
  },
  {
    title:'登录账号',
    dataIndex: 'userAccount',
    valueType: 'text',
    copyable:true
  },
  {
    title:'用户组',
    dataIndex: 'userType',
    valueType: 'text',
    valueEnum: {
      0: {
        text: '普通用户',
      },
      1: {
        text: '管理员',
      },
    }
  },
  {
    title:'用户名',
    dataIndex: 'username',
    valueType: 'text',
    copyable:true
  },
  {
    title: '头像',
    dataIndex: 'avatarUrl',
    valueType: 'image',

  },
  {
    title: '性别',
    dataIndex: 'gender',
    valueType: 'text',
    valueEnum: {
      0: {
        text: '男',
      },
      1: {
        text: '女',
      },
    },
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    valueType: 'text',

    copyable:true
  },
  {
    title: '邮箱',
    dataIndex: 'emile',
    valueType: 'text',

    copyable:true
  },
  {
    title: '用户状态',
    dataIndex: 'userStatus',
    valueType: 'text',
    valueEnum: {
      0: {
        text: '正常',
      },
      1: {
        text: '已封禁',
      },
    },
  },
  {
    title: '注册时间',
    dataIndex: 'createTime',
    valueType: 'dateRange',

  },
  {
    title: '操作时间',
    dataIndex: 'updateTime',
    valueType: 'dateRange',

  },
  {
    title: '操作',
    valueType: 'option',
    key: 'option',
    render: (text, record, _, action) => [
      <a
        key="editable"
        onClick={() => {
          action?.startEditable?.(record.id);
        }}
      >
        编辑
      </a>,
      <a href={record.avatarUrl} target="_blank" rel="noopener noreferrer" key="view">
        查看
      </a>,
      <TableDropdown
        key="actionGroup"
        onSelect={() => action?.reload()}
        menus={[
          { key: 'copy', name: '复制' },
          { key: 'delete', name: '删除' },
        ]}
      />,
    ],
  },
];


export default () => {
  // const actionRef = useRef<ActionType>();
  // actionRef.current?.reload(false);
  return (
    <ProTable<API.CurrentUser>
      columns={columns}
      // actionRef={actionRef}
      cardBordered
      request={async (params, sort, filter) => {
        console.log(sort, filter);
        await waitTime(2000);
        const userList =  await searchAll();
        return {data:userList};
      }}
      editable={{
        type: 'multiple',
      }}
      // columnsState={{
      //   persistenceKey: 'pro-table-singe-demos',
      //   persistenceType: 'localStorage',
      //   onChange(value) {
      //     console.log('value: ', value);
      //   },
      // }}
      rowKey="id"
      search={{
        labelWidth: 'auto',
      }}
      options={{
        setting: {

        },
      }}
      form={{
        // 由于配置了 transform，提交的参与与定义的不同这里需要转化一下
        syncToUrl: (values, type) => {
          if (type === 'get') {
            return {
              ...values,
              created_at: [values.startTime, values.endTime],
            };
          }
          return values;
        },
      }}
      pagination={{
        pageSize: 5,
        onChange: (page) => console.log(page),
      }}
      dateFormatter="string"
      headerTitle="高级表格"
    />
  );
};
