import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { CategoriesAdminComponent } from './admin/categories-admin/categories-admin';
import { adminGuard } from './core/admin-guard';

export const routes: Routes = [
    {
        path: 'login',
        loadComponent: () => import('./auth/login/login').then(m => m.Login)
    },
    {
        path: 'transactions',
        loadComponent: () => import('./transactions/transactions-list/transactions-list').then(m => m.TransactionsList)
    },
    {
        path: 'statistics',
        loadComponent: () => import('./statistics/statistics.component').then(m => m.StatisticsComponent)
    },
    { 
        path: 'admin/categories', 
        component: CategoriesAdminComponent, 
        canActivate: [adminGuard] 
    },
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' }
];