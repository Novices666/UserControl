import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
const Footer: React.FC = () => {
  const defaultMessage = 'CopyRight®Novices';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'NovicesShop',
          title: 'Novices商城',
          href: 'https://novices.cc/',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/novices666',
          blankTarget: true,
        },
        {
          key: 'NovicesMovie',
          title: 'Novices影视',
          href: 'https://www.novices.cc/',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
