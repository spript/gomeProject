let mutations = {};

import { mixin } from 'utils/common.js';

import * as base from './base.js';
import * as account from './account.js';
import * as drawer from './drawer.js';
import * as anicer from './anicer.js';

mixin(mutations, base);
mixin(mutations, account);
mixin(mutations, drawer);
mixin(mutations, anicer);

export default mutations;
