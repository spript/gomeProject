let actions = {};

import { mixin } from 'utils/common.js';

import * as base from './base.js';
import * as account from './account.js';
import * as drawer from './drawer.js';
import * as anicer from './anicer.js';

mixin(actions, base);
mixin(actions, account);
mixin(actions, drawer);
mixin(actions, anicer);

export default actions;
